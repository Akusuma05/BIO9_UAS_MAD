package com.example.uas_mad.view.GameView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Leaderboard;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.Pertanyaan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    public TextView text_waktu, text_money, text_damage, text_pertanyaan, text_upgrade_damage, text_upgrade_health, text_upgrade_info1,  text_upgrade_info2, text_warning;
    public Button btn_jawaban1, btn_jawaban2, btn_jawaban3, btn_jawaban4, btn_skip, btn_buy1, btn_buy2, btn_resume, btn_quit;
    private ProgressBar health_bar_monster;
    private RatingBar health_bar_user;
    private ImageView img_user, img_monster;
    private ScrollView shop;
    private FloatingActionButton btn_pause;
    private View view_pause;
    private String username;

    private GameViewModel gameViewModel;
    private SharedPreferenceHelper helper;

    private List<Pertanyaan.Data> listPertanyaan;
    private List<GameData.Gamedata> listGamedata;
    private List<Monster.Data> listMonster;

    public int waktu, random, monster = 1, health_monster = 100, health_user, current_damage, total_damage, money, current_health_monster = 100;
    private boolean button_pressed = false;
    private CountDownTimer Timer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Deklarasi
        initView();

        gameViewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
        helper = SharedPreferenceHelper.getInstance(requireActivity());
        gameViewModel.init(helper.getAccessToken());

        btn_jawaban1.setVisibility(View.VISIBLE);
        btn_jawaban2.setVisibility(View.VISIBLE);
        btn_jawaban3.setVisibility(View.VISIBLE);
        btn_jawaban4.setVisibility(View.VISIBLE);
        shop.setVisibility(View.INVISIBLE);


        //Game Data
        username = getArguments().getString("student_name");
        int studentid = getArguments().getInt("Student_id");
        gameViewModel.getGamedata(studentid);
        gameViewModel.getResultGameData().observe(getActivity(), showGameData);

        //Pertanyaan
        gameViewModel.getPertanyaan();
        gameViewModel.getResultPertanyaan().observe(getActivity(), showPertanyaan);

        //Monster
        gameViewModel.getMonster();
        gameViewModel.getResultMonster().observe(getActivity(), showMonster);

        //Pause
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer.cancel();

                view_pause.setVisibility(View.VISIBLE);

                btn_resume.setVisibility(View.VISIBLE);
                btn_quit.setVisibility(View.VISIBLE);
                text_pertanyaan.setVisibility(View.INVISIBLE);
                btn_jawaban1.setVisibility(View.INVISIBLE);
                btn_jawaban2.setVisibility(View.INVISIBLE);
                btn_jawaban3.setVisibility(View.INVISIBLE);
                btn_jawaban4.setVisibility(View.INVISIBLE);
                text_warning.setVisibility(View.VISIBLE);
                btn_pause.setEnabled(false);
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer = new CountDownTimer(waktu, 1000) {
                    public void onTick(long millisUntilFinished) {
                        text_waktu.setText(""+String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        waktu = (int) millisUntilFinished;
                    }

                    public void onFinish() {
                        view_pause.setVisibility(View.VISIBLE);
                        text_pertanyaan.setVisibility(View.INVISIBLE);
                        btn_jawaban1.setVisibility(View.INVISIBLE);
                        btn_jawaban2.setVisibility(View.INVISIBLE);
                        btn_jawaban3.setVisibility(View.INVISIBLE);
                        btn_jawaban4.setVisibility(View.INVISIBLE);
                        btn_pause.setEnabled(false);
                        text_warning.setVisibility(View.VISIBLE);
                        text_warning.setText("Game Over Times UPPP!!!");

                        Timer.cancel();

                        Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                        gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                            final Handler handler3 = new Handler();
                            handler3.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Navigation.findNavController(getView()).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                                }
                            }, 5000);
                        });
                    }
                }.start();

                view_pause.setVisibility(View.INVISIBLE);
                btn_resume.setVisibility(View.INVISIBLE);
                btn_quit.setVisibility(View.INVISIBLE);
                text_pertanyaan.setVisibility(View.VISIBLE);
                btn_jawaban1.setVisibility(View.VISIBLE);
                btn_jawaban2.setVisibility(View.VISIBLE);
                btn_jawaban3.setVisibility(View.VISIBLE);
                btn_jawaban4.setVisibility(View.VISIBLE);
                text_warning.setVisibility(View.INVISIBLE);
                btn_pause.setEnabled(true);

            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_menuFragment);
            }
        });


    }

    //Deklarasi
    private void initView() {
        text_damage = getActivity().findViewById(R.id.text_damage);
        text_money = getActivity().findViewById(R.id.text_money);
        text_waktu = getActivity().findViewById(R.id.text_waktu);
        text_pertanyaan = getActivity().findViewById(R.id.text_pertanyaan);
        text_upgrade_damage = getActivity().findViewById(R.id.text_upgrade_damage);
        text_upgrade_health = getActivity().findViewById(R.id.text_upgrade_health);
        text_upgrade_info1 = getActivity().findViewById(R.id.text_upgrade_info1);
        text_upgrade_info2 = getActivity().findViewById(R.id.text_upgrade_info2);
        text_warning = getActivity().findViewById(R.id.text_warning);

        health_bar_monster = getActivity().findViewById(R.id.health_bar_monster);
        health_bar_user = getActivity().findViewById(R.id.health_bar_user);

        btn_jawaban1 = getActivity().findViewById(R.id.btn_jawaban1);
        btn_jawaban2 = getActivity().findViewById(R.id.btn_jawaban2);
        btn_jawaban3 = getActivity().findViewById(R.id.btn_jawaban3);
        btn_jawaban4 = getActivity().findViewById(R.id.btn_jawaban4);
        btn_buy1 = getActivity().findViewById(R.id.btn_buy1);
        btn_buy2 = getActivity().findViewById(R.id.btn_buy2);
        btn_skip = getActivity().findViewById(R.id.btn_skip);
        btn_pause = getActivity().findViewById(R.id.btn_pause);
        btn_resume = getActivity().findViewById(R.id.btn_resume);
        btn_quit = getActivity().findViewById(R.id.btn_quit);

        view_pause = getActivity().findViewById(R.id.view_pause);

        img_monster = getActivity().findViewById(R.id.img_monster);
        img_user = getActivity().findViewById(R.id.img_user);

        shop = getActivity().findViewById(R.id.shop);
    }

    //Observer GameData
    private Observer<GameData> showGameData = new Observer<GameData>() {
        @Override
        public void onChanged(GameData gameData) {
            //Display Waktu Duit sama Damage
            listGamedata = gameData.getGamedata();

            text_damage.setText(String.valueOf(listGamedata.get(0).getTotal_damage()));
            text_waktu.setText("10 : 00");
            text_money.setText(String.valueOf(listGamedata.get(0).getMoney()));

            health_user = listGamedata.get(0).getHealth_left();
            current_damage = listGamedata.get(0).getCurrent_damage();

            health_bar_monster.setProgress(0);
            health_bar_monster.setProgress(100);
            health_bar_user.setRating(health_user);

            //CountDownTimer
            waktu = listGamedata.get(0).getTime_left();
            Timer = new CountDownTimer(waktu, 1000) {
                public void onTick(long millisUntilFinished) {
                    text_waktu.setText(""+String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    waktu = (int) millisUntilFinished;
                }

                public void onFinish() {
                    view_pause.setVisibility(View.VISIBLE);
                    text_pertanyaan.setVisibility(View.INVISIBLE);
                    btn_jawaban1.setVisibility(View.INVISIBLE);
                    btn_jawaban2.setVisibility(View.INVISIBLE);
                    btn_jawaban3.setVisibility(View.INVISIBLE);
                    btn_jawaban4.setVisibility(View.INVISIBLE);
                    btn_pause.setEnabled(false);
                    text_warning.setVisibility(View.VISIBLE);
                    text_warning.setText("Game Over Times UPPP!!!");

                    Timer.cancel();

                    Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                    gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                        final Handler handler3 = new Handler();
                        handler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Navigation.findNavController(getView()).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                            }
                        }, 5000);
                    });
                }

                public void onPause () {

                }
                public void onResume(){
                    onTick(waktu);
                }

            }.start();

        }
    };

    //Observer Pertanyaan
    private Observer<Pertanyaan> showPertanyaan = new Observer<Pertanyaan>() {
        @Override
        public void onChanged(Pertanyaan pertanyaan) {
            //Button Jawaban
            listPertanyaan = pertanyaan.getData();

            int min = 0;
            int max = 56;
            random = new Random().nextInt((max - min) + 1) + min;

            text_pertanyaan.setText(listPertanyaan.get(random).getPertanyaan());

            int rand = new Random().nextInt((4 - 1) + 1) + 1;

            if (rand == 1) {
                btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_benar());
                btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah1());
                btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
            } else if (rand == 2) {
                btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_benar());
                btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
            } else if (rand == 3) {
                btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_benar());
                btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
            } else if (rand == 4) {
                btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah3());
                btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_benar());
            }

            btn_jawaban1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_jawaban1.getText() == listPertanyaan.get(random).getJawaban_benar()) {
                        float temphealth = (float)(((current_health_monster - current_damage)*100)/health_monster);
                        health_bar_monster.setProgress((int)temphealth);
                        current_health_monster = current_health_monster - current_damage;

                        total_damage += current_damage;
                        text_damage.setText(String.valueOf(total_damage));

                        img_user.setImageResource(R.drawable.wizard_attack);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                img_user.setImageResource(R.drawable.wizard_idle);
                            }
                        }, 800);

                        if(current_health_monster <= 0){
                            if(monster == 1) {
                                img_monster.setImageResource(R.drawable.monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 2200);
                            }else if(monster == 2){
                                img_monster.setImageResource(R.drawable.eyeball_monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }else if(monster == 3){
                                img_monster.setImageResource(R.drawable.scifi_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1200);
                            }else if(monster == 4){
                                img_monster.setImageResource(R.drawable.trash_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1100);
                            }else if(monster == 5){
                                img_monster.setImageResource(R.drawable.fire_worm_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }
                            health_monster = health_monster * 2;
                            current_health_monster = health_monster;

                            health_bar_monster.setProgress(100);

                            money += 100;
                            text_money.setText(String.valueOf(money));

                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            shop.setVisibility(View.VISIBLE);

                            text_upgrade_damage.setText("DAMAGE + 15");
                            text_upgrade_health.setText("HEALTH +1");
                            text_upgrade_info1.setText(String.valueOf(current_damage) + " -> " + String.valueOf(current_damage+15));
                            text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));
                            btn_buy1.setText("100");
                            btn_buy2.setText("200");

                            btn_buy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 100){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        current_damage += 15;
                                        money -= 100;
                                        text_money.setText(String.valueOf(money));
                                        btn_jawaban1.setVisibility(View.VISIBLE);
                                        btn_jawaban2.setVisibility(View.VISIBLE);
                                        btn_jawaban3.setVisibility(View.VISIBLE);
                                        btn_jawaban4.setVisibility(View.VISIBLE);
                                        shop.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                            btn_buy2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 200){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(health_user == 3){
                                            Toast.makeText(getContext(), "Your health is full !", Toast.LENGTH_SHORT).show();
                                        }else{
                                            health_user += 1;
                                            health_bar_user.setRating(health_user);
                                            money -= 200;
                                            text_money.setText(String.valueOf(money));
                                            btn_jawaban1.setVisibility(View.VISIBLE);
                                            btn_jawaban2.setVisibility(View.VISIBLE);
                                            btn_jawaban3.setVisibility(View.VISIBLE);
                                            btn_jawaban4.setVisibility(View.VISIBLE);
                                            shop.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }
                            });


                            btn_skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btn_jawaban1.setVisibility(View.VISIBLE);
                                    btn_jawaban2.setVisibility(View.VISIBLE);
                                    btn_jawaban3.setVisibility(View.VISIBLE);
                                    btn_jawaban4.setVisibility(View.VISIBLE);
                                    shop.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    }else{
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;
                        text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                        if(monster == 1) {
                            img_monster.setImageResource(R.drawable.monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.monster_idle);
                                }
                            }, 1500);
                        }else if(monster == 2){
                            img_monster.setImageResource(R.drawable.eyeball_monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                }
                            }, 600);
                        }else if(monster == 3){
                            img_monster.setImageResource(R.drawable.scifi_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                }
                            }, 1180);
                        }else if(monster == 4){
                            img_monster.setImageResource(R.drawable.trash_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                }
                            }, 920);
                        }else if(monster == 5){
                            img_monster.setImageResource(R.drawable.fire_worm_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                }
                            }, 1700);
                        }

                        if(health_user == 0){
                            img_user.setImageResource(R.drawable.wizard_death);
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_user.setImageResource(R.drawable.wizard_death_png);
                                }
                            }, 700);

                            view_pause.setVisibility(View.VISIBLE);
                            text_pertanyaan.setVisibility(View.INVISIBLE);
                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            btn_pause.setEnabled(false);
                            text_warning.setVisibility(View.VISIBLE);
                            text_warning.setText("Game Over U dead!!!");

                            Timer.cancel();

                            Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                            gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                                    }
                                }, 5000);
                            });
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 56;
                        random = new Random().nextInt((max - min) + 1) + min;

                        text_pertanyaan.setText(listPertanyaan.get(random).getPertanyaan());

                        int rand = new Random().nextInt((4 - 1) + 1) + 1;

                        if (rand == 1) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 2) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 3) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 4) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah3());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_benar());
                        }
                    }
                }
            });

            btn_jawaban2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_jawaban2.getText() == listPertanyaan.get(random).getJawaban_benar()) {
                        float temphealth = (float) (((current_health_monster - current_damage)*100)/health_monster);
                        health_bar_monster.setProgress((int)temphealth);
                        current_health_monster = current_health_monster - current_damage;

                        total_damage += current_damage;
                        text_damage.setText(String.valueOf(total_damage));

                        img_user.setImageResource(R.drawable.wizard_attack);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                img_user.setImageResource(R.drawable.wizard_idle);
                            }
                        }, 800);

                        if(current_health_monster <= 0){
                            if(monster == 1) {
                                img_monster.setImageResource(R.drawable.monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 2200);
                            }else if(monster == 2){
                                img_monster.setImageResource(R.drawable.eyeball_monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }else if(monster == 3){
                                img_monster.setImageResource(R.drawable.scifi_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1200);
                            }else if(monster == 4){
                                img_monster.setImageResource(R.drawable.trash_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1100);
                            }else if(monster == 5){
                                img_monster.setImageResource(R.drawable.fire_worm_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }
                            health_monster = health_monster * 2;
                            current_health_monster = health_monster;

                            health_bar_monster.setProgress(100);

                            money += 100;
                            text_money.setText(String.valueOf(money));

                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            shop.setVisibility(View.VISIBLE);

                            text_upgrade_damage.setText("DAMAGE + 15");
                            text_upgrade_health.setText("HEALTH +1");
                            text_upgrade_info1.setText(String.valueOf(current_damage) + " -> " + String.valueOf(current_damage+15));
                            text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                            btn_buy1.setText("100");
                            btn_buy2.setText("200");

                            btn_buy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 100){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        current_damage += 15;
                                        money -= 100;
                                        text_money.setText(String.valueOf(money));
                                        btn_jawaban1.setVisibility(View.VISIBLE);
                                        btn_jawaban2.setVisibility(View.VISIBLE);
                                        btn_jawaban3.setVisibility(View.VISIBLE);
                                        btn_jawaban4.setVisibility(View.VISIBLE);
                                        shop.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                            btn_buy2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 200){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(health_user == 3){
                                            Toast.makeText(getContext(), "Your health is full !", Toast.LENGTH_SHORT).show();
                                        }else{
                                            health_user += 1;
                                            health_bar_user.setRating(health_user);
                                            money -= 200;
                                            text_money.setText(String.valueOf(money));
                                            btn_jawaban1.setVisibility(View.VISIBLE);
                                            btn_jawaban2.setVisibility(View.VISIBLE);
                                            btn_jawaban3.setVisibility(View.VISIBLE);
                                            btn_jawaban4.setVisibility(View.VISIBLE);
                                            shop.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }
                            });

                            btn_skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btn_jawaban1.setVisibility(View.VISIBLE);
                                    btn_jawaban2.setVisibility(View.VISIBLE);
                                    btn_jawaban3.setVisibility(View.VISIBLE);
                                    btn_jawaban4.setVisibility(View.VISIBLE);
                                    shop.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }else{
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;
                        text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                        if(monster == 1) {
                            img_monster.setImageResource(R.drawable.monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.monster_idle);
                                }
                            }, 1500);
                        }else if(monster == 2){
                            img_monster.setImageResource(R.drawable.eyeball_monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                }
                            }, 600);
                        }else if(monster == 3){
                            img_monster.setImageResource(R.drawable.scifi_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                }
                            }, 1180);
                        }else if(monster == 4){
                            img_monster.setImageResource(R.drawable.trash_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                }
                            }, 920);
                        }else if(monster == 5){
                            img_monster.setImageResource(R.drawable.fire_worm_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                }
                            }, 1700);
                        }

                        if(health_user == 0){
                            img_user.setImageResource(R.drawable.wizard_death);
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_user.setImageResource(R.drawable.wizard_death_png);
                                }
                            }, 700);

                            view_pause.setVisibility(View.VISIBLE);
                            text_pertanyaan.setVisibility(View.INVISIBLE);
                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            btn_pause.setEnabled(false);
                            text_warning.setVisibility(View.VISIBLE);
                            text_warning.setText("Game Over U dead!!!");

                            Timer.cancel();

                            Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                            gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                                    }
                                }, 5000);
                            });
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 56;
                        random = new Random().nextInt((max - min) + 1) + min;

                        text_pertanyaan.setText(listPertanyaan.get(random).getPertanyaan());

                        int rand = new Random().nextInt((4 - 1) + 1) + 1;

                        if (rand == 1) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 2) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 3) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 4) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah3());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_benar());
                        }
                    }
                }
            });

            btn_jawaban3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_jawaban3.getText() == listPertanyaan.get(random).getJawaban_benar()) {
                        float temphealth = (float) (((current_health_monster - current_damage)*100)/health_monster);
                        health_bar_monster.setProgress((int) temphealth);
                        current_health_monster = current_health_monster - current_damage;

                        total_damage += current_damage;
                        text_damage.setText(String.valueOf(total_damage));

                        img_user.setImageResource(R.drawable.wizard_attack);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                img_user.setImageResource(R.drawable.wizard_idle);
                            }
                        }, 800);

                        if(current_health_monster <= 0){
                            if(monster == 1) {
                                img_monster.setImageResource(R.drawable.monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 2200);
                            }else if(monster == 2){
                                img_monster.setImageResource(R.drawable.eyeball_monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }else if(monster == 3){
                                img_monster.setImageResource(R.drawable.scifi_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1200);
                            }else if(monster == 4){
                                img_monster.setImageResource(R.drawable.trash_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1100);
                            }else if(monster == 5){
                                img_monster.setImageResource(R.drawable.fire_worm_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }
                            health_monster = health_monster * 2;
                            current_health_monster = health_monster;

                            health_bar_monster.setProgress(100);

                            money += 100;
                            text_money.setText(String.valueOf(money));

                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            shop.setVisibility(View.VISIBLE);

                            text_upgrade_damage.setText("DAMAGE + 15");
                            text_upgrade_health.setText("HEALTH +1");
                            text_upgrade_info1.setText(String.valueOf(current_damage) + " -> " + String.valueOf(current_damage+15));
                            text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                            btn_buy1.setText("100");
                            btn_buy2.setText("200");

                            btn_buy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 100){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        current_damage += 15;
                                        money -= 100;
                                        text_money.setText(String.valueOf(money));
                                        btn_jawaban1.setVisibility(View.VISIBLE);
                                        btn_jawaban2.setVisibility(View.VISIBLE);
                                        btn_jawaban3.setVisibility(View.VISIBLE);
                                        btn_jawaban4.setVisibility(View.VISIBLE);
                                        shop.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                            btn_buy2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 200){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(health_user == 3){
                                            Toast.makeText(getContext(), "Your health is full !", Toast.LENGTH_SHORT).show();
                                        }else{
                                            health_user += 1;
                                            health_bar_user.setRating(health_user);
                                            money -= 200;
                                            text_money.setText(String.valueOf(money));
                                            btn_jawaban1.setVisibility(View.VISIBLE);
                                            btn_jawaban2.setVisibility(View.VISIBLE);
                                            btn_jawaban3.setVisibility(View.VISIBLE);
                                            btn_jawaban4.setVisibility(View.VISIBLE);
                                            shop.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }
                            });
                            btn_skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btn_jawaban1.setVisibility(View.VISIBLE);
                                    btn_jawaban2.setVisibility(View.VISIBLE);
                                    btn_jawaban3.setVisibility(View.VISIBLE);
                                    btn_jawaban4.setVisibility(View.VISIBLE);
                                    shop.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }else{
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;
                        text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                        if(monster == 1) {
                            img_monster.setImageResource(R.drawable.monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.monster_idle);
                                }
                            }, 1500);
                        }else if(monster == 2){
                            img_monster.setImageResource(R.drawable.eyeball_monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                }
                            }, 600);
                        }else if(monster == 3){
                            img_monster.setImageResource(R.drawable.scifi_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                }
                            }, 1180);
                        }else if(monster == 4){
                            img_monster.setImageResource(R.drawable.trash_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                }
                            }, 920);
                        }else if(monster == 5){
                            img_monster.setImageResource(R.drawable.fire_worm_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                }
                            }, 1700);
                        }

                        if(health_user == 0){
                            img_user.setImageResource(R.drawable.wizard_death);
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_user.setImageResource(R.drawable.wizard_death_png);
                                }
                            }, 700);

                            view_pause.setVisibility(View.VISIBLE);
                            text_pertanyaan.setVisibility(View.INVISIBLE);
                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            btn_pause.setEnabled(false);
                            text_warning.setVisibility(View.VISIBLE);
                            text_warning.setText("Game Over U dead!!!");

                            Timer.cancel();

                            Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                            gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                                    }
                                }, 5000);
                            });
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 56;
                        random = new Random().nextInt((max - min) + 1) + min;

                        text_pertanyaan.setText(listPertanyaan.get(random).getPertanyaan());

                        int rand = new Random().nextInt((4 - 1) + 1) + 1;

                        if (rand == 1) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 2) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 3) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 4) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah3());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_benar());
                        }
                    }
                }
            });

            btn_jawaban4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btn_jawaban4.getText() == listPertanyaan.get(random).getJawaban_benar()) {
                        float temphealth = (float) (((current_health_monster - current_damage)*100)/health_monster);
                        health_bar_monster.setProgress((int) temphealth);
                        current_health_monster = current_health_monster - current_damage;

                        total_damage += current_damage;
                        text_damage.setText(String.valueOf(total_damage));

                        img_user.setImageResource(R.drawable.wizard_attack);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                img_user.setImageResource(R.drawable.wizard_idle);
                            }
                        }, 800);

                        if(current_health_monster <= 0){
                            if(monster == 1) {
                                img_monster.setImageResource(R.drawable.monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 2200);
                            }else if(monster == 2){
                                img_monster.setImageResource(R.drawable.eyeball_monster_death);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }else if(monster == 3){
                                img_monster.setImageResource(R.drawable.scifi_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1200);
                            }else if(monster == 4){
                                img_monster.setImageResource(R.drawable.trash_monster_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 1100);
                            }else if(monster == 5){
                                img_monster.setImageResource(R.drawable.fire_worm_death_v2);
                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        monster = new Random().nextInt((4 - 1) + 1) + 1;
                                        if(monster == 1){
                                            img_monster.setImageResource(R.drawable.monster_idle);
                                        }else if(monster == 2){
                                            img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                        }else if(monster == 3){
                                            img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                        }else if(monster == 4){
                                            img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                        }else if(monster == 5){
                                            img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                        }
                                    }
                                }, 900);
                            }
                            health_monster = health_monster * 2;
                            current_health_monster = health_monster;

                            health_bar_monster.setProgress(100);

                            money += 100;
                            text_money.setText(String.valueOf(money));

                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            shop.setVisibility(View.VISIBLE);

                            text_upgrade_damage.setText("DAMAGE + 15");
                            text_upgrade_health.setText("HEALTH +1");
                            text_upgrade_info1.setText(String.valueOf(current_damage) + " -> " + String.valueOf(current_damage+=15));
                            text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                            btn_buy1.setText("100");
                            btn_buy2.setText("200");

                            btn_buy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 100){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        current_damage += 15;
                                        money -= 100;
                                        text_money.setText(String.valueOf(money));
                                        btn_jawaban1.setVisibility(View.VISIBLE);
                                        btn_jawaban2.setVisibility(View.VISIBLE);
                                        btn_jawaban3.setVisibility(View.VISIBLE);
                                        btn_jawaban4.setVisibility(View.VISIBLE);
                                        shop.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });

                            btn_buy2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (money < 200){
                                        Toast.makeText(getContext(), "Your Money is not Enough !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(health_user == 3){
                                            Toast.makeText(getContext(), "Your health is full !", Toast.LENGTH_SHORT).show();
                                        }else{
                                            health_user += 1;
                                            health_bar_user.setRating(health_user);
                                            money -= 200;
                                            text_money.setText(String.valueOf(money));
                                            btn_jawaban1.setVisibility(View.VISIBLE);
                                            btn_jawaban2.setVisibility(View.VISIBLE);
                                            btn_jawaban3.setVisibility(View.VISIBLE);
                                            btn_jawaban4.setVisibility(View.VISIBLE);
                                            shop.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }
                            });

                            btn_skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btn_jawaban1.setVisibility(View.VISIBLE);
                                    btn_jawaban2.setVisibility(View.VISIBLE);
                                    btn_jawaban3.setVisibility(View.VISIBLE);
                                    btn_jawaban4.setVisibility(View.VISIBLE);
                                    shop.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }else{
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;
                        text_upgrade_info2.setText(String.valueOf(health_user) + " -> " + String.valueOf(health_user+1));

                        if(monster == 1) {
                            img_monster.setImageResource(R.drawable.monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.monster_idle);
                                }
                            }, 1500);
                        }else if(monster == 2){
                            img_monster.setImageResource(R.drawable.eyeball_monster_attack);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.eyeball_monster_idle);
                                }
                            }, 600);
                        }else if(monster == 3){
                            img_monster.setImageResource(R.drawable.scifi_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.scifi_monster_idle_v2);
                                }
                            }, 1180);
                        }else if(monster == 4){
                            img_monster.setImageResource(R.drawable.trash_monster_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.trash_monster_idle_v2);
                                }
                            }, 920);
                        }else if(monster == 5){
                            img_monster.setImageResource(R.drawable.fire_worm_attack_v2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_monster.setImageResource(R.drawable.fire_worm_idle_v2);
                                }
                            }, 1700);
                        }

                        if(health_user == 0){
                            img_user.setImageResource(R.drawable.wizard_death);
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    img_user.setImageResource(R.drawable.wizard_death_png);
                                }
                            }, 700);

                            view_pause.setVisibility(View.VISIBLE);
                            text_pertanyaan.setVisibility(View.INVISIBLE);
                            btn_jawaban1.setVisibility(View.INVISIBLE);
                            btn_jawaban2.setVisibility(View.INVISIBLE);
                            btn_jawaban3.setVisibility(View.INVISIBLE);
                            btn_jawaban4.setVisibility(View.INVISIBLE);
                            btn_pause.setEnabled(false);
                            text_warning.setVisibility(View.VISIBLE);
                            text_warning.setText("Game Over U dead!!!");

                            Timer.cancel();

                            Leaderboard.Data leaderboard = addLeaderboard(username, total_damage);
                            gameViewModel.createLeaderboard(leaderboard).observe(requireActivity(), leaderboard1->{
                                final Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_leaderboardFragment);
                                    }
                                }, 5000);
                            });
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 56;
                        random = new Random().nextInt((max - min) + 1) + min;

                        text_pertanyaan.setText(listPertanyaan.get(random).getPertanyaan());

                        int rand = new Random().nextInt((4 - 1) + 1) + 1;

                        if (rand == 1) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 2) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 3) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_benar());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_salah3());
                        } else if (rand == 4) {
                            btn_jawaban1.setText(listPertanyaan.get(random).getJawaban_salah1());
                            btn_jawaban2.setText(listPertanyaan.get(random).getJawaban_salah2());
                            btn_jawaban3.setText(listPertanyaan.get(random).getJawaban_salah3());
                            btn_jawaban4.setText(listPertanyaan.get(random).getJawaban_benar());
                        }
                    }
                }
            });
        }
    };


    //Observer Monster
    public Observer<Monster> showMonster = new Observer<Monster>() {
        @Override
        public void onChanged(Monster monster) {
//            text_money.setText(monster.getData().get(0).getMonster_name());
        }
    };


    private Leaderboard.Data addLeaderboard(String username, int total_damage){
        Leaderboard.Data leaderboard = new Leaderboard.Data(username, total_damage);
        return leaderboard;
    }

}
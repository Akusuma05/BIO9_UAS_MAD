package com.example.uas_mad.view.GameView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.model.PertanyaanTerjawab;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    public TextView text_waktu, text_money, text_damage, text_pertanyaan;
    public Button btn_jawaban1, btn_jawaban2, btn_jawaban3, btn_jawaban4;
    private ProgressBar health_bar_monster;
    private RatingBar health_bar_user;
    private ImageView img_user, img_monster;

    private GameViewModel gameViewModel;
    private SharedPreferenceHelper helper;
    private List<Pertanyaan.Data> listPertanyaan;
    private List<GameData.Gamedata> listGamedata;
    private List<Item.Data> listItem;
    private List<Monster.Data> listMonster;
    private List<PertanyaanTerjawab.Data> listTerjawab;
    private List<ItemTerbeli.Data> listTerbeli;
    public int waktu, random, monster = 1, health_monster = 100, health_user, current_damage, total_damage, money, current_health_monster = 100;
    boolean button_pressed = false;


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

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        gameViewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
        gameViewModel.init(helper.getAccessToken());

//        GameData.Gamedata gamedata = addGamedata(3, 1, 1 , 1, 1, 1, 1);
//        gameViewModel.createGamedata(gamedata).observe(requireActivity(), gamedata1 -> {
//            if (gamedata1 != null){
//                Toast.makeText(requireActivity(), "Add Gamedata Success", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(requireActivity(), "Add Gamedata Failed", Toast.LENGTH_SHORT).show();
//            }
//        });

        //Game Data
        int studentid = getArguments().getInt("Student_id");
        gameViewModel.getGamedata(studentid);
        gameViewModel.getResultGameData().observe(getActivity(), showGameData);

        //Pertanyaan
        gameViewModel.getPertanyaan();
        gameViewModel.getResultPertanyaan().observe(getActivity(), showPertanyaan);

        //Item
        gameViewModel.getItem();
        gameViewModel.getResultItem().observe(getActivity(), showItem);

        //Monster
        gameViewModel.getMonster();
        gameViewModel.getResultMonster().observe(getActivity(), showMonster);

        //Terbunuh
        gameViewModel.getTerbunuh();
        gameViewModel.getResultTerbunuh().observe(getActivity(), showTerbunuh);

        //Terjawab
        gameViewModel.getTerjawab();
        gameViewModel.getResultTerjawab().observe(getActivity(), showTerjawab);

        //Terbeli
        gameViewModel.getTerbeli();
        gameViewModel.getResultTerbeli().observe(getActivity(), showTerbeli);

    }

    //Deklarasi
    private void initView() {
        text_damage = getActivity().findViewById(R.id.text_damage);
        text_money = getActivity().findViewById(R.id.text_money);
        text_waktu = getActivity().findViewById(R.id.text_waktu);
        text_pertanyaan = getActivity().findViewById(R.id.text_pertanyaan);

        health_bar_monster = getActivity().findViewById(R.id.health_bar_monster);
        health_bar_user = getActivity().findViewById(R.id.health_bar_user);

        btn_jawaban1 = getActivity().findViewById(R.id.btn_jawaban1);
        btn_jawaban2 = getActivity().findViewById(R.id.btn_jawaban2);
        btn_jawaban3 = getActivity().findViewById(R.id.btn_jawaban3);
        btn_jawaban4 = getActivity().findViewById(R.id.btn_jawaban4);

        img_monster = getActivity().findViewById(R.id.img_monster);
        img_user = getActivity().findViewById(R.id.img_user);
    }

    //Observer GameData
    private Observer<GameData> showGameData = new Observer<GameData>() {
        @Override
        public void onChanged(GameData gameData) {
            //Display Waktu Duit sama Damage
            listGamedata = gameData.getGamedata();

            text_damage.setText(String.valueOf(listGamedata.get(0).getTotal_damage()));
            text_waktu.setText(String.valueOf(listGamedata.get(0).getTime_left()));
            text_money.setText(String.valueOf(listGamedata.get(0).getMoney()));

            health_user = listGamedata.get(0).getHealth_left();
            current_damage = listGamedata.get(0).getCurrent_damage();

            health_bar_monster.setProgress(100);
            health_bar_user.setRating(health_user);

        }
    };

    //Observer Pertanyaan
    private Observer<Pertanyaan> showPertanyaan = new Observer<Pertanyaan>() {
        @Override
        public void onChanged(Pertanyaan pertanyaan) {
            //Button Jawaban
            listPertanyaan = pertanyaan.getData();

            int min = 0;
            int max = 1;
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
                        Toast.makeText(getContext(), "jawaban 1 benar", Toast.LENGTH_SHORT).show();
                        health_bar_monster.setProgress((current_health_monster - current_damage)*(100/health_monster));
                        Toast.makeText(getContext(), String.valueOf(current_health_monster), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), String.valueOf(current_damage), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), String.valueOf(health_monster), Toast.LENGTH_SHORT).show();
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

                        if(current_health_monster == 0){
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
                        }
                    }else{
                        Toast.makeText(getContext(), "jawaban 1 salah", Toast.LENGTH_SHORT).show();
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;

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
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 1;
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
                        Toast.makeText(getContext(), "jawaban 2 benar", Toast.LENGTH_SHORT).show();
                        health_bar_monster.setProgress((current_health_monster - current_damage)*(100/health_monster));
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

                        if(current_health_monster == 0){
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
                        }
                    }else{
                        Toast.makeText(getContext(), "jawaban 2 salah", Toast.LENGTH_SHORT).show();
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;

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
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 1;
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
                        Toast.makeText(getContext(), "jawaban 3 benar", Toast.LENGTH_SHORT).show();
                        health_bar_monster.setProgress((current_health_monster - current_damage)*(100/health_monster));
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

                        if(current_health_monster == 0){
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
                        }
                    }else{
                        Toast.makeText(getContext(), "jawaban 3 salah", Toast.LENGTH_SHORT).show();
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;

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
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 1;
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
                        Toast.makeText(getContext(), "jawaban 4 benar", Toast.LENGTH_SHORT).show();
                        health_bar_monster.setProgress((current_health_monster - current_damage)*(100/health_monster));
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

                        if(current_health_monster == 0){
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
                        }
                    }else{
                        Toast.makeText(getContext(), "jawaban 4 salah", Toast.LENGTH_SHORT).show();
                        health_bar_user.setRating(health_user - 1);
                        health_user -= 1;

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
                        }
                    }
                    button_pressed = true;
                    if(button_pressed = true) {
                        button_pressed = false;
                        int min = 0;
                        int max = 1;
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

    //Observer Item
    private Observer<Item> showItem = new Observer<Item>(){
        @Override
        public void onChanged(Item item) {
//            text_damage.setText(item.getData().get(0).getItem_name());
        }
    };

    //Observer Monster
    public Observer<Monster> showMonster = new Observer<Monster>() {
        @Override
        public void onChanged(Monster monster) {
//            text_money.setText(monster.getData().get(0).getMonster_name());
        }
    };

    //Observer Terbunuh
    public Observer<MonsterTerbunuh> showTerbunuh = new Observer<MonsterTerbunuh>() {
        @Override
        public void onChanged(MonsterTerbunuh monsterTerbunuh) {
//            text_waktu.setText(String.valueOf(monsterTerbunuh.getData().get(0).getMonster_id_terbunuh()));
        }
    };

    //Observer Terjawab
    public Observer<PertanyaanTerjawab> showTerjawab = new Observer<PertanyaanTerjawab>() {
        @Override
        public void onChanged(PertanyaanTerjawab pertanyaanTerjawab) {
//            btn_jawaban1.setText(String.valueOf(pertanyaanTerjawab.getData().get(0).getPertanyaan_id_terjawab()));
        }
    };

    //Observer Terbeli
    public Observer<ItemTerbeli> showTerbeli = new Observer<ItemTerbeli>() {
        @Override
        public void onChanged(ItemTerbeli itemTerbeli) {
//            btn_jawaban2.setText(String.valueOf(itemTerbeli.getData().get(0).getHarga()));
        }
    };

    private GameData.Gamedata addGamedata(int student_gamedata_id, int student_id_gamedata, int total_damage, int health_left, int money, int time_left, int current_damage) {
        GameData.Gamedata gamedata = new GameData.Gamedata(student_gamedata_id, student_id_gamedata, total_damage, health_left, money, time_left, current_damage);
        return gamedata;
    }

}
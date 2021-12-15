package com.example.uas_mad.view.GameView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.model.PertanyaanTerjawab;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    public TextView text_waktu, text_money, text_damage;
    public Button btn_jawaban1, btn_jawaban2, btn_jawaban3, btn_jawaban4;

    private GameViewModel gameViewModel;
    private SharedPreferenceHelper helper;
    private List<Pertanyaan.Data> listPertanyaan;
    private List<GameData.Gamedata> listGamedata;
    private List<Item.Data> listItem;
    private List<Monster.Data> listMonster;
    private List<PertanyaanTerjawab.Data> listTerjawab;
    private List<ItemTerbeli.Data> listTerbeli;

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

        btn_jawaban1 = getActivity().findViewById(R.id.btn_jawaban1);
        btn_jawaban2 = getActivity().findViewById(R.id.btn_jawaban2);
        btn_jawaban3 = getActivity().findViewById(R.id.btn_jawaban3);
        btn_jawaban4 = getActivity().findViewById(R.id.btn_jawaban4);
    }

    //Observer GameData
    private Observer<GameData> showGameData = new Observer<GameData>() {
        @Override
        public void onChanged(GameData gameData) {
            //Display Waktu Duit sama Damage
            listGamedata = gameData.getGamedata();

//            text_damage.setText(String.valueOf(listGamedata.get(0).getTotal_damage()));
//            text_waktu.setText(String.valueOf(listGamedata.get(0).getTime_left()));
//            text_money.setText(String.valueOf(listGamedata.get(0).getMoney()));

        }
    };

    //Observer Pertanyaan
    private Observer<Pertanyaan> showPertanyaan = new Observer<Pertanyaan>() {
        @Override
        public void onChanged(Pertanyaan pertanyaan) {
            //Button Jawaban
            listPertanyaan = pertanyaan.getData();

//            btn_jawaban1.setText(listPertanyaan.get(0).getJawaban_benar());
//            btn_jawaban2.setText(listPertanyaan.get(0).getJawaban_salah1());
            btn_jawaban3.setText(listPertanyaan.get(0).getJawaban_salah2());
            btn_jawaban4.setText(listPertanyaan.get(0).getJawaban_salah3());

        }
    };

    //Observer Item
    private Observer<Item> showItem = new Observer<Item>(){
        @Override
        public void onChanged(Item item) {
            text_damage.setText(item.getData().get(0).getItem_name());
        }
    };

    //Observer Monster
    public Observer<Monster> showMonster = new Observer<Monster>() {
        @Override
        public void onChanged(Monster monster) {
            text_money.setText(monster.getData().get(0).getMonster_name());
        }
    };

    //Observer Terbunuh
    public Observer<MonsterTerbunuh> showTerbunuh = new Observer<MonsterTerbunuh>() {
        @Override
        public void onChanged(MonsterTerbunuh monsterTerbunuh) {
            text_waktu.setText(String.valueOf(monsterTerbunuh.getData().get(0).getMonster_id_terbunuh()));
        }
    };

    //Observer Terjawab
    public Observer<PertanyaanTerjawab> showTerjawab = new Observer<PertanyaanTerjawab>() {
        @Override
        public void onChanged(PertanyaanTerjawab pertanyaanTerjawab) {
            btn_jawaban1.setText(String.valueOf(pertanyaanTerjawab.getData().get(0).getPertanyaan_id_terjawab()));
        }
    };

    //Observer Terbeli
    public Observer<ItemTerbeli> showTerbeli = new Observer<ItemTerbeli>() {
        @Override
        public void onChanged(ItemTerbeli itemTerbeli) {
            btn_jawaban2.setText(String.valueOf(itemTerbeli.getData().get(0).getHarga()));
        }
    };

}
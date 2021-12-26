package com.example.uas_mad.view.MenuView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.Profile;
import com.example.uas_mad.view.GameView.GameViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private Button btn_logout, btn_play, btn_leaderboard;

    private MenuViewModel menuViewModel;
    private SharedPreferenceHelper helper;
    private static final String TAG = "Menu Fragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Game_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Deklarasi
        InitView();

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        menuViewModel = new ViewModelProvider(getActivity()).get(MenuViewModel.class);
        menuViewModel.init(helper.getAccessToken());
        menuViewModel.getStudentData();
        menuViewModel.getResultStudentData().observe(getActivity(), showprofile);


        //Logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_logout){
                    menuViewModel.logout().observe(requireActivity(), s ->{
                        if (!s.isEmpty()) {
                            helper.clearPref();
                            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_loginFragment);
                            Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_leaderboardFragment);
            }
        });

    }

    //Fungsi Deklarasi
    private void InitView() {
        btn_logout = getActivity().findViewById(R.id.btn_logout);
        btn_play = getActivity().findViewById(R.id.btn_play);
        btn_leaderboard = getActivity().findViewById(R.id.btn_leaderboard);
    }

    //Button Play Ditekan
    private Observer<Profile> showprofile = new Observer<Profile>() {
        @Override
        public void onChanged(Profile profile) {
            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("student_name", profile.getUsername());
                    bundle.putInt("Student_id", profile.getId());
                    Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_gameFragment, bundle);
                }
            });
        }
    };
}
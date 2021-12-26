package com.example.uas_mad.view.Leaderboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.Leaderboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {

    private TextView text_leaderboard;
    private FloatingActionButton btn_back_leaderboard;
    private RecyclerView rv_leaderboard;

    private Leaderboard_adapter adapter;
    private LeaderboardViewModel leaderboardViewModel;
    private SharedPreferenceHelper helper;
    private static final String TAG = "Leaderboard Fragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leaderboard.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
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
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Deklarasi
        initview();

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        leaderboardViewModel = new ViewModelProvider(getActivity()).get(LeaderboardViewModel.class);
        leaderboardViewModel.init(helper.getAccessToken());
        leaderboardViewModel.getLeaderboard();
        leaderboardViewModel.getResultLeaderboard().observe(getActivity(), showleaderboard);

        btn_back_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_leaderboardFragment_to_menuFragment);
            }
        });
    }

    private void initview() {
        rv_leaderboard = getActivity().findViewById(R.id.rv_leaderboard);
        btn_back_leaderboard = getActivity().findViewById(R.id.btn_back_leaderboard);
    }

    private Observer<Leaderboard> showleaderboard = new Observer<Leaderboard>() {
        @Override
        public void onChanged(Leaderboard leaderboard) {
            adapter = new Leaderboard_adapter(leaderboard.getData());
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            rv_leaderboard.setLayoutManager(manager);
            rv_leaderboard.setAdapter(adapter);
        }
    };
}
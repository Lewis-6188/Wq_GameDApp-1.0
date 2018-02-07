package com.game.wanq.uu.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.wanq.app.R;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class UserPLFragment extends Fragment {

    public UserPLFragment newInstance() {
        return new UserPLFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gren_szhipl_layout, container, false);
        return view;
    }

}

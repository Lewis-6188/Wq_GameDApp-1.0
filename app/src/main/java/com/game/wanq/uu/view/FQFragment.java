package com.game.wanq.uu.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.wanq.app.R;
import com.game.wanq.uu.model.GanmeFQScrollViewAdapter;
import com.game.wanq.uu.view.whget.GameFQScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis.Liu on 2017/12/27.
 */

public class FQFragment extends Fragment implements GameFQScrollView.OnItemClickListener {
    public FQFragment newInstance() {
        return new FQFragment();
    }

    private GanmeFQScrollViewAdapter ganmeFQScrollViewAdapter;
    private GameFQScrollView gameFQScrollView;
    private List<String> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wanq_gamexq_tabfq, container, false);
        gameFQScrollView = (GameFQScrollView) view.findViewById(R.id.id_gamefqScrollView);
        mDatas = new ArrayList<>();
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        mDatas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514260887870&di=4d7c744bbbaf68e219c635474d900af0&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa8773912b31bb05137ecb7cd3c7adab44bede00f.jpg");
        ganmeFQScrollViewAdapter = new GanmeFQScrollViewAdapter(getActivity(), mDatas);
        gameFQScrollView.initDatas(ganmeFQScrollViewAdapter, mDatas.size());
        gameFQScrollView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onClickFqGame(View view, int pos) {
//        LinearLayout tvShow = (LinearLayout) view.findViewById(R.id.llayout);
//        tvShow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.fqbg));
    }
}

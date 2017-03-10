package com.dhanarjkusuma.sarihusada.sarihusada.view.mypro;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.mypro.fragments.MyProFragmentHistory;
import com.dhanarjkusuma.sarihusada.sarihusada.view.mypro.fragments.MyProFragmentUpdate;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsUbahPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.fragments.HistoryUpdatePeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.fragments.RepsFormUpdatePeserta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyProUbahPeserta extends AppCompatActivity {

    private ViewPager viewPager;
    private Peserta peserta;
    private List<Bundle> revisi = Collections.emptyList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro_ubah_peserta);
        setTitle("Update Peserta");
        viewPager =(ViewPager)findViewById(R.id.viewpager);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            peserta = bundle.getParcelable("peserta");
            if(peserta.getRevisi().size() > 0){
                revisi = new ArrayList<>();
                for(int i=0;i<peserta.getRevisi().size();i++){
                    Bundle revisiBundle = new Bundle();
                    revisiBundle.putParcelable("revisi", peserta.getRevisi().get(i));
                    revisi.add(revisiBundle);
                }
            }
        }

        if(viewPager!=null)
        {
            setUpViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        PesertaPagerAdapter adapter=new PesertaPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putParcelable("peserta", peserta);
        adapter.addFragment(new MyProFragmentUpdate(),"Update", bundle);
        for(int i=0; i<revisi.size(); i++){
            adapter.addFragment(new MyProFragmentHistory(), "History " + (i+1), revisi.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    static class PesertaPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<Bundle> mBundles = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public PesertaPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(Fragment fragment, String title, Bundle bundle) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
            mBundles.add(bundle);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f = mFragments.get(position);
            f.setArguments(mBundles.get(position));
            return f;
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}

package mx.edu.chmd2;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import mx.edu.chmd2.adapter.TabAdapter;
import mx.edu.chmd2.fragment.CompartidosFragment;
import mx.edu.chmd2.fragment.FavoritosFragment;
import mx.edu.chmd2.fragment.LeidosFragment;
import mx.edu.chmd2.fragment.NoLeidosFragment;
import mx.edu.chmd2.fragment.TodasCircularesFragment;

public class CircularActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    TextView lblEncabezado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CircularActivity.this, MenuCircularesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lblEncabezado = toolbar.findViewById(R.id.lblTextoToolbar);
        lblEncabezado.setText("Circulares");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodasCircularesFragment(), "Todas");
        adapter.addFragment(new LeidosFragment(), "Leídas");
        adapter.addFragment(new NoLeidosFragment(), "No Leídas");
        adapter.addFragment(new FavoritosFragment(), "Favoritas");
        adapter.addFragment(new CompartidosFragment(), "Compartidas");
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }





    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_circular, container, false);

            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}

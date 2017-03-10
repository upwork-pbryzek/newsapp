package android.ali.com.briefme.fragments;

import android.ali.com.briefme.R;
import android.ali.com.briefme.activities.MainActivity;
import android.ali.com.briefme.adapters.NewsAdapter;
import android.ali.com.briefme.data.OverviewObj;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Created by Paul on 3/8/17.
 */

public class NewsFragment extends Fragment {

    private NewsFragment.OnFragmentInteractionListener mListener;

    private ListView listview;

    // get data from the table by the ListAdapter
    private NewsAdapter listAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void clearNewsList() {
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
    }

    public void addNewsRowToListView(OverviewObj obj) {
        listAdapter.addRow(obj);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getArguments() != null) {
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        listview = (ListView) view.findViewById(R.id.listview);

        // get data from the table by the ListAdapter
        listAdapter = new NewsAdapter();

        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OverviewObj rowObj = (OverviewObj) listAdapter.getItem(position);
                MainActivity activity = (MainActivity) getActivity();
                activity.handleListClick(rowObj);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsFragment.OnFragmentInteractionListener) {
            mListener = (NewsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

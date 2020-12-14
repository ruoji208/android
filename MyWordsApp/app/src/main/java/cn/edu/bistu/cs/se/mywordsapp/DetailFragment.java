package cn.edu.bistu.cs.se.mywordsapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.bistu.cs.se.mywordsapp.contract.notes;


public class DetailFragment extends Fragment {
    private static final String TAG="myTag";
    public static final String ARG_ID = "id";

    private String mID;//单词主键
    private OnFragmentInteractionListener mListener;//本Fragment所在的Activity

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity与Fragment之间的传值
        if (getArguments() != null) {
            mID = getArguments().getString(ARG_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail, container, false);
        Log.v(TAG,mID);

        NoteDB noteDB = NoteDB.getWordsDB();

        if(noteDB !=null && mID!=null){
            TextView textViewWord=(TextView)view.findViewById(R.id.word);
            TextView textViewWordMeaning=(TextView)view.findViewById(R.id.wordmeaning);
            TextView textViewWordSample=(TextView)view.findViewById(R.id.wordsample);

            notes.WordDescription item= noteDB.getSingleWord(mID);
            if(item!=null){
                textViewWord.setText(item.title);
                textViewWordMeaning.setText(item.author);
                textViewWordSample.setText(item.content);
            } else{
                textViewWord.setText("");
                textViewWordMeaning.setText("");
                textViewWordSample.setText("");
            }

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onWordDetailClick(Uri uri);

    }

}

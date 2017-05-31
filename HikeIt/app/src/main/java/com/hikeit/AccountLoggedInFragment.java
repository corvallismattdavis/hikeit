package com.hikeit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountLoggedInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountLoggedInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountLoggedInFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    public AccountLoggedInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountLoggedInFragment newInstance() {
        AccountLoggedInFragment fragment = new AccountLoggedInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void signoutClicked (View view) {
        auth = FirebaseAuth.getInstance();
        auth.signOut();

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onStart()
    {
        super.onStart();
        TextView text = (TextView) getView().findViewById(R.id.account_name);

        text.setText(auth.getCurrentUser().getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onAccountLoggedInFragmentInteraction(string);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        public void onAccountLoggedInFragmentInteraction(String string);
    }
}

package ir.coleo.varam.activities.login_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;

/**
 * صفحه ثبت نام کاربر جدید
 */
public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_sign_up, container, false);

        view.findViewById(R.id.submit).setOnClickListener(v -> {

        });

        return view;
    }
}
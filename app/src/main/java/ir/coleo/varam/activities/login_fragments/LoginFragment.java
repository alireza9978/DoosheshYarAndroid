package ir.coleo.varam.activities.login_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.LoginActivity;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_login, container, false);

        view.findViewById(R.id.submit).setOnClickListener(v -> {
            ((LoginActivity) requireActivity()).goApp();

        });
        view.findViewById(R.id.forgot_password).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ForgetPasswordActivity.class);
            startActivity(intent);
        });

        return view;
    }

}

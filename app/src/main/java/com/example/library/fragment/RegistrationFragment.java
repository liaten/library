package com.example.library.fragment;

import static com.example.library.helper.DateHelper.*;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.R;
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.DateHelper;
import com.example.library.mail.JavaMailAPI;

public class RegistrationFragment extends FragmentWithHeader {

    private DatePickerDialog datePickerDialog;
    private Button dateBirthButton, approveButton;
    private EditText SurnameEditText, NameEditText, PatronymicEditText, PhoneNumberEditText,
            RegAddressEditText, EmailEditText, PasswordEditText;
    private String surname, name, patronymic, phone, regAddress, email, password, SQLDateBirth;
    private String username = "test";

    private final OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            SQLDateBirth = getSQLDate((short)day, (short)month, (short)year);
            String dateShowToUser = day + " " + DateHelper.getRussianMonthsGenitive((short)(month+1)) + " " + year;
            dateBirthButton.setText(dateShowToUser);
            dateBirthButton.setTextColor(Color.BLACK);
        }
    };
    private final View.OnClickListener dateBirthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            datePickerDialog.show();
        }
    };
    private final View.OnClickListener approveButtonListener = view -> {
        DatabaseHelper db = new DatabaseHelper(this.getContext());
        surname = SurnameEditText.getText().toString();
        name = NameEditText.getText().toString();
        patronymic = PatronymicEditText.getText().toString();
        phone = PhoneNumberEditText.getText().toString();
        regAddress = RegAddressEditText.getText().toString();
        email = EmailEditText.getText().toString();
        password = PasswordEditText.getText().toString();
        if(surname.equals("") || name.equals("") || patronymic.equals("") || phone.equals("") || regAddress.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(getActivity(),"Не все поля заполнены",Toast.LENGTH_SHORT).show();
        }
        else {
            db.addUser(username,password,surname,name,patronymic,phone,SQLDateBirth,email,regAddress);
            sendEmail();
            Toast.makeText(getActivity(), "Проверьте ваш email", Toast.LENGTH_SHORT).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSpinner();
        setViews();
    }

    private void setViews() {
        dateBirthButton = getView().findViewById(R.id.date_birth_button);
        approveButton = getView().findViewById(R.id.approve);
        SurnameEditText = getView().findViewById(R.id.surname_edit_text);
        NameEditText = getView().findViewById(R.id.name_edit_text);
        PatronymicEditText = getView().findViewById(R.id.patronymic_edit_text);
        PhoneNumberEditText = getView().findViewById(R.id.phone_number_edit_text);
        RegAddressEditText = getView().findViewById(R.id.registration_address_edit_text);
        EmailEditText = getView().findViewById(R.id.email_edit_text);
        PasswordEditText = getView().findViewById(R.id.password_edit_text);

        approveButton.setOnClickListener(approveButtonListener);
        dateBirthButton.setOnClickListener(dateBirthClickListener);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener,
                getYear(), getMonth()-1, getDay());
        datePickerDialog.setTitle("Дата рождения");
    }

    private void setSpinner() {
        Spinner spinner = getView().findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sendEmail() {
        String mSubject = "Успешная регистрация";
        String mMessage = "Вами был успешно создан аккаунт в детской библиотеке им. Маршака " +
                "через мобильное приложение" +
                "\nВаш логин для входа в приложение: "
                + username
                + "\nВаш email: " + email
                + "\nВаш пароль для входа в приложение: " + password
                + "\nИспользуйте только один вариант: логин в окне авторизации."
                + "\nВ целях безопасности никому не сообщайте данные из этого сообщения.";
        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), email, mSubject, mMessage);
        javaMailAPI.execute();
    }
}
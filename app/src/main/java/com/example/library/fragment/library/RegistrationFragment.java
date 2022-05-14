package com.example.library.fragment.library;

import static com.example.library.helper.DateHelper.getDay;
import static com.example.library.helper.DateHelper.getMonth;
import static com.example.library.helper.DateHelper.getSQLDate;
import static com.example.library.helper.DateHelper.getYear;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.CreateUser;
import com.example.library.helper.DateHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.SearchEmailByEmailFromDB;
import com.example.library.mail.JavaMailAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment implements AsyncResponse {

    @Nullable
    protected DatePickerDialog datePickerDialog;
    @Nullable
    protected Button dateBirthButton, approveButton;
    @Nullable
    protected EditText SurnameEditText, NameEditText, PatronymicEditText, PhoneNumberEditText,
            EmailEditText;
    @Nullable
    protected Spinner genderSpinner;
    private static final String NAME_EMAIL_DB = "email";
    private View view;
    private static final String CREATE_USER_DB = "create_user";
    private final TextWatcher cyrillicTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for (EditText et : cyrillicEditTexts) {

                if (!Pattern.compile("^[А-Я][а-я]*$").matcher(et.getText().toString()).find()) {
                    if (et.getText().toString().length() > 0) {
                        char ch = et.getText().toString().charAt(et.getText().toString().length() - 1);
                        if (ch >= 'а' && ch <= 'я') {
                            et.setText(String.format("%s", (char) ((int) ch - 32)));
                        } else {
                            et.setText(et.getText().toString().substring(0,
                                    et.getText().toString().length() - 1));
                        }
                        et.setSelection(et.getText().toString().length());
                    }
                    if(et.equals(SurnameEditText)){
                        SurnameHintTextView.setText(R.string.hint_cyrillic_names);
                        SurnameHintTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.date_secondary));
                    }
                    else if(et.equals(NameEditText)){
                        NameHintTextView.setText(R.string.hint_cyrillic_names);
                        NameHintTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.date_secondary));
                    }
                    else{
                        PatronymicHintTextView.setText(R.string.hint_cyrillic_names);
                        PatronymicHintTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.date_secondary));
                    }
                }
                else {
                    if(et.equals(SurnameEditText)){
                        SurnameHintTextView.setText("Фамилия введена верно");
                        SurnameHintTextView.setTextColor(Color.parseColor("#008c00"));
                    }
                    else if(et.equals(NameEditText)){
                        NameHintTextView.setText("Имя введено верно");
                        NameHintTextView.setTextColor(Color.parseColor("#008c00"));
                    }
                    else{
                        PatronymicHintTextView.setText("Отчество введено верно");
                        PatronymicHintTextView.setTextColor(Color.parseColor("#008c00"));
                    }
                }
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    protected String surname, name, patronymic, phone_str, email, SQLDateBirth, password, userid, gender;
    List<EditText> cyrillicEditTexts;
    @Nullable
    protected CheckBox applyCheckBox;
    private final TextWatcher numberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = PhoneNumberEditText.getText().toString();
            if (!Pattern.compile("^\\+7[0-9]{0,10}$").matcher(text).find()) {
                if (text.length() < 3) {
                    PhoneNumberEditText.setText("+7");
                } else {
                    PhoneNumberEditText.setText(PhoneNumberEditText.getText().toString().substring(0,
                            PhoneNumberEditText.getText().toString().length() - 1));
                }
                PhoneNumberEditText.setSelection(PhoneNumberEditText.getText().toString().length());
            }
            text = PhoneNumberEditText.getText().toString();
            if (text.length() == 12) {
                PhoneHintTextView.setText("Телефон введён верно");
                PhoneHintTextView.setTextColor(Color.parseColor("#008c00"));
            }
            else {
                PhoneHintTextView.setText(R.string.phone_hint);
                PhoneHintTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.date_secondary));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private static final String TAG = "RegistrationFragment";

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setSpinner();
        setOnClickListeners();
        setApplyCheckBox();
        setDatePickerDialog();
        setCyrillicEditTexts();
        addTextChangeListeners();
    }

    private final OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            SQLDateBirth = getSQLDate((short) day, (short) (month+1), (short) year);
            String dateShowToUser = day + " " +
                    DateHelper.getRussianMonthsGenitive((short) (month + 1)) + " " + year;
            dateBirthButton.setText(dateShowToUser);
            dateBirthButton.setTextColor(Color.BLACK);
        }
    };

    protected TextView SurnameHintTextView, NameHintTextView, PatronymicHintTextView,
            PhoneHintTextView, EmailHintTextView, DateHintTextView;

    private final View.OnClickListener dateBirthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            datePickerDialog.show();
        }
    };

    private final View.OnClickListener applyCheckboxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            approveButton.setEnabled(applyCheckBox.isChecked());
        }
    };


    private final View.OnFocusChangeListener numberFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                String text = PhoneNumberEditText.getText().toString();
                if(text.length()==0){
                    PhoneNumberEditText.setText("+7");
                    PhoneNumberEditText.setSelection(PhoneNumberEditText.getText().toString().length());
                }
            }
        }
    };
    private final View.OnClickListener approveButtonListener = view -> {

        surname = SurnameEditText.getText().toString();
        name = NameEditText.getText().toString();
        patronymic = PatronymicEditText.getText().toString();
        gender = genderSpinner.getSelectedItem().toString();
        phone_str = PhoneNumberEditText.getText().toString();
        email = EmailEditText.getText().toString();

        switch (gender){
            case "Мужской":
                gender = "m";
                break;
            case "Женский":
                gender = "f";
                break;
        }
        Log.d(TAG, ": " + gender);
        if (surname.equals("") || name.equals("") || patronymic.equals("") || phone_str.equals("")
                || email.equals("")) {
            Toast.makeText(getActivity(), "Не все поля заполнены", Toast.LENGTH_SHORT).show();
        } else {
            Random random = new Random();
            int user_id = random.nextInt(2147483647);
            userid = String.valueOf(user_id);
            password = String.valueOf(random.nextInt(2147483647));
            if (isMailValid(email)) {
                GetEmailFromDB(email);
            } else {
                Toast.makeText(getActivity(), "Почта введена неверно",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean isMailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private void setViews() {
        view = requireView();
        dateBirthButton = view.findViewById(R.id.date_birth_button);
        approveButton = view.findViewById(R.id.approve);
        SurnameEditText = view.findViewById(R.id.surname_edit_text);
        NameEditText = view.findViewById(R.id.name_edit_text);
        PatronymicEditText = view.findViewById(R.id.patronymic_edit_text);
        PhoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);
        EmailEditText = view.findViewById(R.id.email_edit_text);
        applyCheckBox = view.findViewById(R.id.checkbox_apply);
        SurnameHintTextView = view.findViewById(R.id.surname_hint);
        NameHintTextView = view.findViewById(R.id.name_hint);
        PatronymicHintTextView = view.findViewById(R.id.patronymic_hint);
        PhoneHintTextView = view.findViewById(R.id.phone_hint);
        EmailHintTextView = view.findViewById(R.id.email_hint);
        DateHintTextView = view.findViewById(R.id.date_birth_hint);
        genderSpinner = view.findViewById(R.id.gender_spinner);
    }

    private void setOnClickListeners() {
        approveButton.setOnClickListener(approveButtonListener);
        dateBirthButton.setOnClickListener(dateBirthClickListener);
        applyCheckBox.setOnClickListener(applyCheckboxClickListener);
    }

    private void createUser(String surname, String name, String patronymic, String phone,
                            String SQLDateBirth, String email, String user_id, String password, String gender) {
        new CreateUser(this).execute(surname, name, patronymic, phone, SQLDateBirth, email,
                user_id, password, gender);
    }

    private void GetEmailFromDB(String email) {
        new SearchEmailByEmailFromDB(this).execute(email);
    }

    private void setApplyCheckBox() {
        applyCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
        String URL = "https://www.nbrkomi.ru/gfx/soglasieru.doc";
        String checkBoxText = "Я согласен(на) на обработку персональных данных<br> <a href='" +
                URL + "' download>Согласие</a>";
        applyCheckBox.setText(Html.fromHtml(checkBoxText, Html.FROM_HTML_MODE_LEGACY));
    }

    private void setCyrillicEditTexts() {
        cyrillicEditTexts = Arrays.asList(SurnameEditText,NameEditText,PatronymicEditText);
    }

    private void addTextChangeListeners(){
        for (EditText et : cyrillicEditTexts){
            et.addTextChangedListener(cyrillicTextWatcher);
        }
        PhoneNumberEditText.addTextChangedListener(numberTextWatcher);
        PhoneNumberEditText.setOnFocusChangeListener(numberFocusListener);
    }

    private void setDatePickerDialog(){
        int style = android.R.style.Theme_Material_Light_Dialog_Alert;
        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener,
                getYear(), getMonth(), getDay());
        datePickerDialog.setTitle("Дата рождения");
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    }

    private void setSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void sendEmail() {
        String mSubject = "Успешная регистрация";
        String mMessage = "Вами был успешно создан аккаунт в детской библиотеке им. Маршака " +
                "через мобильное приложение"
                + "\nВаш email: " + email
                + "\nВаш логин для входа в приложение: " + userid
                + "\nВаш временный пароль для входа в приложение: " + password
                + "\nИспользуйте только один вариант: логин в окне авторизации."
                + "\nВ целях безопасности никому не сообщайте данные из этого сообщения.";
        JavaMailAPI javaMailAPI;
        if (email != null) {
            javaMailAPI = new JavaMailAPI(email, mSubject, mMessage);
            javaMailAPI.execute();
        }
    }

    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {

    }

    @Override
    public void processFinish(Bitmap output) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {
        try {
            Log.d(TAG, "returnJSONObject: " + jsonObject);
            if (jsonObject.getBoolean("success")) {
                String type = jsonObject.getString("type");
                switch (type) {
                    case NAME_EMAIL_DB:
                        Toast.makeText(getActivity(),
                                "Аккаунт под такой электронной почтой уже зарегистрирован",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case CREATE_USER_DB:
                        int regs_num = MainActivity.getSP().getInt("regs_num", 0) + 1;
                        MainActivity.getSP().edit().putInt("regs_num", regs_num).apply();
                        sendEmail();
                        Toast.makeText(getActivity(),
                                "Пароль отправлен на\n" + email, Toast.LENGTH_SHORT).show();
                        new FragmentHelper((MainActivity) requireActivity(),
                                false,true)
                                .execute(new AuthorizationFragment());
                        break;
                }
            } else {
                String type = jsonObject.getString("type");
                if (type.equals(NAME_EMAIL_DB)) {
                    createUser(surname, name, patronymic, phone_str, SQLDateBirth, email, userid, password, gender);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
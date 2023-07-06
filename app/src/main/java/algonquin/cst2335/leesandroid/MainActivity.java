package algonquin.cst2335.leesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class prompts and accepts the user password and validates the input.
 * @author Aliah Smith
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Shows what the your is required to enter in the edit text
     */
    private TextView tv = null;
    /**
     * Accepts the input from the user
     */
    private EditText et = null;
    /**
     * checks if the password meets the criteria
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

    btn.setOnClickListener(clk ->{
        String password = et.getText().toString();
        checkPasswordComplexity(password);
        if (checkPasswordComplexity( password)) {

            tv.setText("Your password meets the requirements");
        }else {
            tv.setText("You shall not pass!");
        }

    });

}

    /**
     * This function displays a message when password criteria is not met
     * @param pw the String object that we are checking
     * @return returns true if the password is complex enough
     */
    boolean checkPasswordComplexity(String pw){

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for(int i=0; i<pw.length(); i++) {
            char c = pw.charAt(i);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else {
                foundSpecial = isSpecialCharacter(c);
            }
        }
            if (!foundUpperCase) {

                String message = "Your password does not have an upper case letter";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;

            } else if (!foundLowerCase) {
                String message = "Your password does not have a lower case letter";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();// Say that they are missing a lower case letter;

                return false;

            } else if (!foundNumber) {
                String message = "Your password does not have a number";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;

            } else if (!foundSpecial) {
                String message = "Your password does not have a special character";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return false;
            }
            else
                return true;

    }

    /**
     * This function checks if special characters have been entered in the password from the user
     * @param c Character being checked for special character
     * @return whether or not a special character was found
     */
    boolean isSpecialCharacter(char c)    {
        switch (c){
            case '#':
            case '?':
            case '$':
            case '%':
            case '^':
            case '&':
            case '!':
            case '@':
                return  true;
            default:
                return false;
        }


    }
    }
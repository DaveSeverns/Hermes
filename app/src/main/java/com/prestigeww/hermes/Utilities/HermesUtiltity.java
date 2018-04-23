package com.prestigeww.hermes.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.util.Patterns;
import android.widget.Toast;



public class HermesUtiltity {

    public HermesUtiltity(Context mContext) {
        this.mContext = mContext;
    }

    private final String EMERGENCY_COLOR = "Red" ;
    private final String MAINTENANCE_COLOR = "Yellow" ;
    private final String FREE_STUFF_COLOR = "Blue";
    private final String mFavoritesFile = "favorites_list.ser";

    private Context mContext;

    private Location mLocation;



    public boolean isValidEmail(String emailAddress) {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    /*
     * Between 6 and 21 characters
     * A password must contain at least 1 digit and 1 special character
     */
    public boolean isValidPassword(String password) {
        boolean special = false, digit = false, upperCase = false, lowerCase = false;

        if (password != null && password.length() > 5 && password.length() < 21) {
            for (int index = 0; index < password.length(); index++) {
                final Character character = password.charAt(index);
                if (Character.isWhitespace(character))
                    return false;
                else if (Character.isLowerCase(character))
                    lowerCase = true;
                else if (Character.isUpperCase(character))
                    upperCase = true;
                else if (Character.isDigit(character))
                    digit = true;
                else if (33 <= character.charValue() && 127 >= character.charValue())
                    special = true;
            }
        }
        return special && digit && upperCase && lowerCase;
    }

    public String formatEmail(String email) {
        String lowerEmail = email.toLowerCase();
        StringBuilder formattedEmail = new StringBuilder();

        for (int i = 0; i < lowerEmail.length(); i++) {
            if (lowerEmail.charAt(i) == ' ') {
            } else {
                formattedEmail.append(lowerEmail.charAt(i));
            }
        }
        return formattedEmail.toString();
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}

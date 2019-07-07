package Functionality_Class;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialougeAlert {

    public String AlertMe(Context context, int title, Object message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title).setMessage(message.toString()).setCancelable(false);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                return;
            }
        });

        alertDialog.show();
        return null;

    }
}

package hse.t.targetscomplete.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import hse.t.targetscomplete.R;


public class SpinnerDialog {
    Activity context;
    String dTitle, closeTitle="ЗАКРЫТЬ";
    AlertDialog alertDialog;
    int pos;
    int style;

    public SpinnerDialog(Activity activity, String dialogTitle) {
        this.context = activity;
        this.dTitle = dialogTitle;
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View v = context.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView rippleViewClose = (TextView) v.findViewById(R.id.close);
        TextView title = (TextView) v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText(closeTitle);
        title.setText(dTitle);

        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.getWindow().getAttributes().windowAnimations = style;//R.style.DialogAnimations_SmileWindow;
        alertDialog.setCancelable(false);

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

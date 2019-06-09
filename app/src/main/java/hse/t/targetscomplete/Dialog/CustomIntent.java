package hse.t.targetscomplete.Dialog;

import android.app.Activity;
import android.content.Context;

import hse.t.targetscomplete.R;


public class CustomIntent {
    public static String[] types = {
            "",
            "",

    };

    public static void customType(Context context, String animtype){
        Activity act = (Activity)context;
        switch (animtype){
            case "fadein-to-fadeout":
                act.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            default:
                break;

        }
    }
}

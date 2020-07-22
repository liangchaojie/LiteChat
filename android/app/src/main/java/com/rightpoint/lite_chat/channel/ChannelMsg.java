package com.rightpoint.lite_chat.channel;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.rightpoint.lite_chat.IM.IMsgReceiver;
import com.rightpoint.lite_chat.IM.IMsgSender;
import com.rightpoint.lite_chat.IM.huanxin.HXReceiver;
import com.rightpoint.lite_chat.IM.huanxin.HXSender;
import com.rightpoint.lite_chat.MainActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/7/18 7:49 AM 
 */
public class ChannelMsg {
    static final String CHANNEL_CALL_NATIVE = "com.rightpoint.litechat/msg";
    static final String CHANNEL_NATIVE_CALL = "com.rightpoint.litechat/receive_msg";

    public static void connect(MainActivity activity) {
        receiveMsg(activity);
        resolveSendMsg(activity);
    }

    private static void resolveSendMsg(MainActivity activity) {
        IMsgSender sender = new HXSender();

        new MethodChannel(activity.getFlutterView(), CHANNEL_CALL_NATIVE).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call,
                                     @NonNull MethodChannel.Result result) {
                if ("sendTxt".equals(call.method)) {
                    String txt = call.argument("txt");
                    String username = call.argument("username");
                    String isGroup = call.argument("isGroup");

                    if (TextUtils.isEmpty(txt) || TextUtils.isEmpty(username)) {
                        return;
                    }

                    sender.sendTxt(username, !TextUtils.isEmpty(isGroup), txt);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.success("success");
                        }
                    });
                }
            }
        });
    }

    private static void receiveMsg(MainActivity activity) {
        MethodChannel methodChannel = new MethodChannel(activity.getFlutterView(),
                ChannelMsg.CHANNEL_NATIVE_CALL);

        IMsgReceiver msgReceiver = new HXReceiver();

        msgReceiver.registerMsgListener(new IMsgReceiver.MsgReceiverListener() {
            @Override
            public void receiveTxt(String from, String txt) {
                Map<String, String> msg = new HashMap<>(2);

                msg.put("from", from);
                msg.put("txt", txt);

                methodChannel.invokeMethod("receiveTxtMsg", msg);
            }

            @Override
            public void receiveImg(String from, boolean original, Uri thumbUri, Uri imgUri) {

            }

            @Override
            public void receiveVoice(String from, int length, Uri voiceUri) {

            }

            @Override
            public void receiveVideo(String from, int length, Uri thumbUri, Uri videoUri) {

            }

            @Override
            public void receiveFile(String from, Uri fileUri) {

            }
        });

        WeakReference<FlutterActivity> reference = new WeakReference<>(activity);

        msgReceiver.startListening(reference);
    }
}

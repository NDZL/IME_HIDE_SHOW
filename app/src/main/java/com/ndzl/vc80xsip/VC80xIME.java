package com.ndzl.vc80xsip;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


//README: per non far comparire il pulsante 'scelta keyboard',
//semplicemente disabilitare tutte le G* keyboard sotto menu Apps

public class VC80xIME extends InputMethodService
    implements OnKeyboardActionListener{
     
    private KeyboardView kv;
    private Keyboard keyboard;
     
    private boolean caps = true;
    
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.layout.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    public void onStartInputView (EditorInfo info, boolean restarting){
        super.onStartInputView(info, restarting);
        if((info.inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER){
            keyboard = new Keyboard(this, R.layout.numeric);
            kv.setKeyboard(keyboard);
            kv.closing();
//            final  InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
//            mInputView.setSubtypeOnSpaceKey(subtype);
        }
        else{

//           keyboard = new Keyboard(this, R.layout.qwerty);
            keyboard = new Keyboard(this, R.layout.onekey);
            kv.setKeyboard(keyboard);
            kv.closing();
        }




    }


    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
        case 32: 
            am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
            break;
        case Keyboard.KEYCODE_DONE:
        case 10: 
            am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
            break;
        case Keyboard.KEYCODE_DELETE:
            am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
            break;              
        default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }       
    }

    boolean toggleKeyboard=true;
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {        
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
        case Keyboard.KEYCODE_DELETE :
            ic.deleteSurroundingText(1, 0);
            break;
        case Keyboard.KEYCODE_SHIFT:
            caps = !caps;
            keyboard.setShifted(caps);
            kv.invalidateAllKeys();
            break;
        case Keyboard.KEYCODE_DONE:
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            break;

        case 142:
            //ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_F12));  //ex key F12 a sx di Z
            if(!toggleKeyboard){
                keyboard = new Keyboard(this, R.layout.onekey);
                kv.setKeyboard(keyboard);
                kv.closing();
            }
            else{
                keyboard = new Keyboard(this, R.layout.qwerty);
                kv.setKeyboard(keyboard);
                kv.closing();
            }
            toggleKeyboard = !toggleKeyboard;
            break;

        default:
            //Toast.makeText(this, "|"+primaryCode+"|"+keyCodes.length+"|" , Toast.LENGTH_SHORT).show();



            char code = (char)primaryCode;
            if(Character.isLetter(code) && caps){
                code = Character.toUpperCase(code);
            }
            ic.commitText(String.valueOf(code),1);

        }
    }
 
    @Override
    public void onPress(int primaryCode) {
    }
 
    @Override
    public void onRelease(int primaryCode) {            
    }
 
    @Override
    public void onText(CharSequence text) {     
    }
 
    @Override
    public void swipeDown() {   
    }
 
    @Override
    public void swipeLeft() {
    }
 
    @Override
    public void swipeRight() {
    }
 
    @Override
    public void swipeUp() {
    }
}
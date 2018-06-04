package imagisoft.modelview;

import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.CardView;
import android.support.design.widget.TextInputEditText;

import java.util.Calendar;
import butterknife.BindView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import imagisoft.model.Message;


public class ChatFragment extends MessagesFragment {

    /**
     * Se obtiene el usuario actual o que envía
     */
    protected FirebaseAuth auth = FirebaseAuth.getInstance();
    protected FirebaseUser user = auth.getCurrentUser();

    /**
     * Botón e input para enviar los mensajes
     */
    @BindView(R.id.send_card_view)
    CardView sendCardView;

    @BindView(R.id.text_input_view)
    TextInputEditText textInputView;

    /**
     * Se define cúal es el layout que va a utilizar
     */
    @Override
    public void setupResource() {
        this.resource = R.layout.chat_view;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupSendCardView();
    }

    @Override
    public void setupActivityView() {
        setToolbarText(R.string.nav_chat);
        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.GONE);
        super.setupActivityView();
    }

    /**
     * Guarda lo que el usuario ha escrito en el chat para cuando regrese
     * al mismo fragmento o se gire la pantalla
     * @param outState: Bundle con el contenido del chat
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String text = textInputView.getEditableText().toString();
        outState.putString("text_input", text);
    }

    /**
     * Restaura en lo que el usuario habia escrito en el chat después
     * de salirse de la panralla o al girarla
     * @param savedInstanceState: Bundle con el contenido del chat
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            String text = savedInstanceState.getString("text_input");
            textInputView.setText(text);
            messagesRV.scrollToPosition(messagesVA.getItemCount() - 1);
        }
    }

    /**
     * Se prepara el adaptador para poder recibir nuevas vistas de mensajes. Si
     * el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @Override
    public void setupAdapter(){
        if(messagesVA == null) {
            messagesVA = new ChatAdapter(this);
            registerAdapterDataObserver();
        }
    }

    /**
     * Al presionar el botón se reunen los datos del msg y luego se envía
     */
    private void setupSendCardView() {
        sendCardView.setOnClickListener(v -> sendMessage());
    }

    /**
     * Función para enviar un msg. Toma el contenido y si no está vacío
     * procede a enviarlo
     */
    public void sendMessage(){
        String content = textInputView.getText().toString();
        if (!content.isEmpty()) sendNotEmptyMessage(content);
    }

    /**
     * Función usada por sendMessage
     * Se revisa que el mesaje no este vacío previamente
     * @param content: Contenido del mensaje extraido del input
     */
    private void sendNotEmptyMessage(String content){
        Message msg = createMessage(content);
        activity.getChatReference().push().setValue(msg);
        textInputView.setText("");
    }

    /**
     * Reúne los datos del mensaje; usuario y fecha
     * y crea el objeto.
     * @param content: Contenido del mensaje extraido del input
     */
    public Message createMessage(String content){
        String username = getCurrentUsername();
        Long datetime = Calendar.getInstance().getTimeInMillis();
        return new Message(user.getUid(), username, content, datetime);
    }

}
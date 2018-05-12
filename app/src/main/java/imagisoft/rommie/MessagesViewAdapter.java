package imagisoft.rommie;

import android.view.View;
import android.widget.TextView;
import android.text.util.Linkify;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Message;
import imagisoft.edepa.Timestamp;
import imagisoft.miscellaneous.DateConverter;


public abstract class MessagesViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Último mensaje enviado en el congreso
     */
    protected Message lastMessage;

    /**
     * Referencia al objeto que adapta
     */
    protected MainActivityFragment view;

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    protected ArrayList<Timestamp> msgs;

    /**
     * Se obtiene el usuario actual o que envía
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Constructor del adaptador
     */
    public MessagesViewAdapter(MainActivityFragment view){
        this.view = view;
        this.msgs = new ArrayList<>();
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    /**
     * Se enlazan los componentes
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Timestamp timestamp = msgs.get(viewHolder.getAdapterPosition());

        if(timestamp instanceof Message) {

            Message msg = (Message) timestamp;
            MessageViewHolder holder = (MessageViewHolder) viewHolder;
            holder.msgUsername.setText(msg.getUsername());
            holder.msgContent.setText(msg.getContent());
            holder.msgTimeDescription.setText(DateConverter.extractTime(msg.getTime()));
            Linkify.addLinks(holder.msgContent, Linkify.WEB_URLS);

        }

        else {

            TimestampViewHolder holder = (TimestampViewHolder) viewHolder;

            Long currentTime = Calendar.getInstance().getTimeInMillis();
            String currentDate = DateConverter.extractDate(currentTime);
            String lastTimestampDate = DateConverter.extractDate(timestamp.getTime());

            if (currentDate.equals(lastTimestampDate))
                holder.timeSeparator.setText(view.getResources().getString(R.string.text_today));
            else
                holder.timeSeparator.setText(DateConverter.extractDate(timestamp.getTime()));

        }

    }

    /**
     * Clase para dividir los mensajes por hora
     */
    protected class TimestampViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_separator_time)
        TextView timeSeparator;

        TimestampViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    /**
     * Clase para enlazar los mensajes a sus resptivas vistas
     */
    protected class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.msg_username)
        TextView msgUsername;

        @BindView(R.id.msg_content)
        TextView msgContent;

        @BindView(R.id.msg_time_description)
        TextView msgTimeDescription;

        MessageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
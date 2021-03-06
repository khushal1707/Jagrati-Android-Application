package com.example.jagratiapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.QuestionAddPage;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.graphics.Color.rgb;

public class QuestionAddAdapter extends RecyclerView.Adapter<QuestionAddAdapter.ViewHolder> {
    private Context context;
    private List<Question> questionList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflator;
    private Button editLongPress;
    private Button deleteLongPress;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String classid;
    private String quizid;

    public QuestionAddAdapter(Context context, List<Question> questionList, String classid, String quizid) {
        this.context = context;
        this.questionList = questionList;
        this.classid = classid;
        this.quizid = quizid;
    }


    public QuestionAddAdapter() {
    }

    @NonNull
    @Override
    public QuestionAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAddAdapter.ViewHolder holder, int position) {
        Question ques = questionList.get(position);
        holder.question.setText((++position) + ") " + ques.getQuestion());
        holder.option1.setText("(a) " + ques.getOption1());
        holder.option2.setText("(b) " + ques.getOption2());
        holder.option3.setText("(c) " + ques.getOption3());
        holder.option4.setText("(d) " + ques.getOption4());
        holder.quesId  = ques.getQuestionId();

        if (ques.getCorrectOption().equals(ques.getOption1()))
            holder.option1.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption2()))
            holder.option2.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption3()))
            holder.option3.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption4()))
            holder.option4.setTextColor(rgb(0, 128, 0));




    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private TextView option1;
        private TextView option2;
        private TextView option3;
        private TextView option4;
        private String correctOption;
        private EditText questionPopup;
        private EditText quesPopup;
        private EditText option1Popup;
        private EditText option2Popup;
        private EditText option3Popup;
        private EditText option4Popup;
        private RadioButton option1RadioButton;
        private RadioButton option2RadioButton;
        private RadioButton option3RadioButton;
        private RadioButton option4RadioButton;
        private Button savequesButton;
        private String quesId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.QuesIn_ques_card);
            option1 = itemView.findViewById(R.id.Opt1In_ques_card);
            option2 = itemView.findViewById(R.id.Opt2In_ques_card);
            option3 = itemView.findViewById(R.id.Opt3In_ques_card);
            option4 = itemView.findViewById(R.id.Opt4In_ques_card);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    createPopup();
                    return false;
                }
            });
        }

        private void createPopup() {
            builder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);
            final View view = inflator.inflate(R.layout.onlongpress_popup, null);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            editLongPress = view.findViewById(R.id.edit_longpress);
            deleteLongPress = view.findViewById(R.id.delete_longpress);
            editLongPress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Question ques = questionList.get(getAdapterPosition());
                    editques(ques);
                }
            });

            deleteLongPress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    createWarningPopup(getAdapterPosition());
                }
            });
        }

        private void editques(Question ques) {
            builder = new AlertDialog.Builder(context);
            inflator = LayoutInflater.from(context);
            final View view = inflator.inflate(R.layout.popup_add_question, null);

            questionPopup= view.findViewById(R.id.QuesIn_ques_card_popup);
            option1Popup = view.findViewById(R.id.Opt1In_ques_card_popup);
            option2Popup = view.findViewById(R.id.Opt2In_ques_card_popup);
            option3Popup = view.findViewById(R.id.Opt3In_ques_card_popup);
            option4Popup = view.findViewById(R.id.Opt4In_ques_card_popup);
            option1RadioButton = view.findViewById(R.id.optionA_question_card_popup);
            option2RadioButton = view.findViewById(R.id.optionB_question_card_popup);
            option3RadioButton = view.findViewById(R.id.optionC_question_card_popup);
            option4RadioButton = view.findViewById(R.id.optionD_question_card_popup);
            savequesButton = view.findViewById(R.id.saveques_popup);

            questionPopup.setText(ques.getQuestion());
            option1Popup.setText(ques.getOption1());
            option2Popup.setText(ques.getOption2());
            option3Popup.setText(ques.getOption3());
            option4Popup.setText(ques.getOption4());
            correctOption = ques.getCorrectOption();

            if (correctOption.equals(ques.getOption1())) {
                option1RadioButton.setChecked(true);
            } else if (correctOption.equals(ques.getOption2())) {
                option2RadioButton.setChecked(true);
            } else if (correctOption.equals(ques.getOption3())) {
                option3RadioButton.setChecked(true);
            } else {
                option4RadioButton.setChecked(true);
            }

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            savequesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(questionPopup.getText().toString().trim())
                            && !TextUtils.isEmpty(option1Popup.getText().toString().trim())
                            && !TextUtils.isEmpty(option2Popup.getText().toString().trim())
                            && !TextUtils.isEmpty(option3Popup.getText().toString().trim())
                            && !TextUtils.isEmpty(option4Popup.getText().toString().trim())
                            && (option1RadioButton.isChecked()
                            || option2RadioButton.isChecked()
                            || option3RadioButton.isChecked()
                            || option4RadioButton.isChecked())) {
                        dialog.dismiss();
                        if (option1RadioButton.isChecked())
                            correctOption = option1Popup.getText().toString();
                        else if (option2RadioButton.isChecked())
                            correctOption = option2Popup.getText().toString();
                        else if (option3RadioButton.isChecked())
                            correctOption = option3Popup.getText().toString();
                        else if (option4RadioButton.isChecked())
                            correctOption = option4Popup.getText().toString();

                        Question ques = new Question(questionPopup.getText().toString().trim(), option1Popup.getText().toString().trim(),
                                option2Popup.getText().toString().trim(), option3Popup.getText().toString().trim(), option4Popup.getText().toString().trim(),
                                correctOption, null);


                        updateQuestion(ques);
                    } else {
                        Toast.makeText(context, "Enter everything", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void updateQuestion(Question ques) {
            final Question q = ques;

            db.collection("Classes").document(classid).collection("Quizzes").document(quizid).collection("Question").document(quesId)
                    .set(q).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    questionList.get(getAdapterPosition()).setQuestion(questionPopup.getText().toString().trim());
                    questionList.get(getAdapterPosition()).setOption1(option1Popup.getText().toString());
                    questionList.get(getAdapterPosition()).setOption2(option2Popup.getText().toString());
                    questionList.get(getAdapterPosition()).setOption3(option3Popup.getText().toString());
                    questionList.get(getAdapterPosition()).setOption4(option4Popup.getText().toString());
                    notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

        private void createWarningPopup(final int adapterPosition) {
            Button yes,no;
            TextView warningMessage;
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.delete_popup,null);

            yes = view.findViewById(R.id.confirm_delete);
            no= view.findViewById(R.id.cancel_delete);
            warningMessage = view.findViewById(R.id.warning_delete);
            warningMessage.setText("You want to delete " );
            builder.setView(view);
            final Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                    db.collection("Classes").document(classid).collection("Quizzes").document(quizid)
                            .collection("Question").document(quesId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    questionList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                }
            });
        }

    }


}

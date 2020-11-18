package com.example.notes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.model.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Note> notes;
    private ArrayList<Note> allNotes;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Note> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()) {
                filteredList.addAll(allNotes);
            } else {
                for(Note note : allNotes) {
                    if(note.getTag().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(note);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            notes.clear();
            notes.addAll((Collection<? extends Note>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final String UID_KEY = "UID_KEY";
        public static final String TITLE_KEY = "TITLE_KEY";
        public static final String TAG_KEY = "TAG_KEY";
        public static final String TEXT_KEY = "TEXT_KEY";
        public TextView tvNote;
        public TextView tvTag;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNote = (TextView)itemView.findViewById(R.id.tvNote);
            tvTag = (TextView)itemView.findViewById(R.id.tvTag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getLayoutPosition();
            Note note = notes.get(position);

            Intent intent = new Intent(v.getContext(), ViewNote.class);
            intent.putExtra(UID_KEY, note.getUid());
            intent.putExtra(TITLE_KEY, note.getTitle());
            intent.putExtra(TAG_KEY, note.getTag());
            intent.putExtra(TEXT_KEY, note.getText());
            v.getContext().startActivity(intent);
        }
    }

    public NotesAdapter(ArrayList<Note> notes) {
        this.notes = notes;
        this.allNotes = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.tvNote.setText(note.getTitle());
        holder.tvTag.setText(note.getTag());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

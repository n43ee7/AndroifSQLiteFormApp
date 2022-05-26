package com.napps.project;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class PersonAdapter extends ArrayAdapter<Person> {
    private int resourceId;
    public PersonAdapter(Context context, int textViewResourceId,
                         List<Person> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView personId = (TextView) view.findViewById(R.id.person_id);
        TextView personName = (TextView) view.findViewById(R.id.person_name);
        personId.setText(person.getId());
        personName.setText(person.getName());
        return view;
    }
}
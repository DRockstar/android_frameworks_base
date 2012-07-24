/*
 * Copyright (C) 2008 Esmertec AG.
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.text.method;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.app.AlertDialog;
import android.app.Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SmileyParser;
import android.widget.SimpleAdapter;

    private void showSmileyDialog() {
        if (mSmileyDialog == null) {
            int[] icons = SmileyParser.DEFAULT_SMILEY_RES_IDS;
            String[] names = getResources().getStringArray(
                    SmileyParser.DEFAULT_SMILEY_NAMES);
            final String[] texts = getResources().getStringArray(
                    SmileyParser.DEFAULT_SMILEY_TEXTS);

            final int N = names.length;

            List<Map<String, ?>> entries = new ArrayList<Map<String, ?>>();
            for (int i = 0; i < N; i++) {
                // We might have different ASCII for the same icon, skip it if
                // the icon is already added.
                boolean added = false;
                for (int j = 0; j < i; j++) {
                    if (icons[i] == icons[j]) {
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    HashMap<String, Object> entry = new HashMap<String, Object>();

                    entry. put("icon", icons[i]);
                    entry. put("name", names[i]);
                    entry.put("text", texts[i]);

                    entries.add(entry);
                }
            }

            final SimpleAdapter a = new SimpleAdapter(
                    this,
                    entries,
                    R.layout.smiley_menu_item,
                    new String[] {"icon", "name", "text"},
                    new int[] {R.id.smiley_icon, R.id.smiley_name, R.id.smiley_text});
            SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView) {
                        Drawable img = getResources().getDrawable((Integer)data);
                        ((ImageView)view).setImageDrawable(img);
                        return true;
                    }
                    return false;
                }
            };
            a.setViewBinder(viewBinder);

            AlertDialog.Builder b = new AlertDialog.Builder(this);

            b.setTitle(getString(R.string.menu_insert_smiley));

            b.setCancelable(true);
            b.setAdapter(a, new DialogInterface.OnClickListener() {
                @SuppressWarnings("unchecked")
                public final void onClick(DialogInterface dialog, int which) {
                    HashMap<String, Object> item = (HashMap<String, Object>) a.getItem(which);

                    String smiley = (String)item.get("text");
                    if (mSubjectTextEditor != null && mSubjectTextEditor.hasFocus()) {
                        mSubjectTextEditor.append(smiley);
                    } else {
                        mTextEditor.append(smiley);
                    }

                    dialog.dismiss();
                }
            });

            mSmileyDialog = b.create();
        }

        mSmileyDialog.show();
    }


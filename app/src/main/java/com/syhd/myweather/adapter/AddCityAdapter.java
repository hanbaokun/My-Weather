/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.syhd.myweather.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syhd.myweather.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 城市适配器类
 *
 * @author 咖枯
 * @version 1.0 2015/11/08
 */
public class AddCityAdapter extends MyBaseAdapter<String> {

    /**
     * 城市适配器构造方法
     *
     * @param context context
     * @param list    城市列表
     */
    public AddCityAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder localViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gv_add_city, null);
            localViewHolder = new ViewHolder(convertView);
            convertView.setTag(localViewHolder);
        } else {
            localViewHolder = (ViewHolder) convertView.getTag();
        }
        localViewHolder.cityName.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.city_name)
        TextView cityName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

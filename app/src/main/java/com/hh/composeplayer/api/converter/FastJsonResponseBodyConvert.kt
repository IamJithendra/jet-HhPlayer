package com.hh.composeplayer.api.converter;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @ProjectName: ZngjMvvM
 * @Package: com.zngj.zngjmvvm.util.http.converter
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/1/15  10:29
 */
final class FastJsonResponseBodyConvert<T> implements Converter<ResponseBody, T> {

    private Type type;

    public FastJsonResponseBodyConvert(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String s = value.string();
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            if (jsonObject.optInt("code") == 401 || jsonObject.optString("message").equals("Unauthorized")) {
//                if (AppManager.getAppManager().getContextToken() != null) {
//                    if(AppManager.getAppManager().getContextToken() instanceof BaseActivity){
//                        Intent intent = new Intent(AppManager.getAppManager().getContextToken(), LoginActivityVMK.class);
//                        intent.putExtra("isfalg", false);
//                        ((Activity)AppManager.getAppManager().getContextToken()).startActivityForResult(intent, Extra.ISLOGINCODE);
//                    }
//                    else if(AppManager.getAppManager().getContextToken() instanceof BaseActivity){
//                        Intent intent = new Intent(AppManager.getAppManager().getContextToken(), LoginActivityVMK.class);
//                        intent.putExtra("isfalg", false);
//                        ((Activity)AppManager.getAppManager().getContextToken()).startActivityForResult(intent, Extra.ISLOGINCODE);
//                    }
//                }
//                return null;
//            } else {
//                return JSON.parseObject(s, type);
//            }
//        } catch (JSONException e) {
//            return JSON.parseObject(s, type);
//        }
        if("class java.lang.Object".equals(type.toString())){
           return (T)s;
        }

        return JSON.parseObject(s, type);
    }
}
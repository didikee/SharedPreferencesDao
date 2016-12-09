# SharedPreferencesDao
# SharedPreferences 的另一种场景的用法

> 昨天,下班在家想做什么来着,然后想用`SharedPreferences`存点数据,但是不知道咋地突然想到,`SharedPreferences`是应用启动时一次性加到内存里的.适合少量的存储,多的话还是用数据库吧.实际项目中都是数据库,我私下写写demo就不搞那么麻烦了=.=
>
> 然后,问题来了,如果我要使用`SharedPreferences`,如何在比较小的单位里存储更多的信息.

> 好了,正题.

## 1. 昨天谷歌提供了国内的 `android developer`官网!!!
{=.=} 激动!!!

你的书签地址还是 **[https://developer.android.com/index.html](https://developer.android.com/index.html)** ???{大雾}

国内速度更快!!!地址:**[https://developer.android.google.cn/index.html](https://developer.android.google.cn/index.html)**{**快快加书签!!!**}

##2. 最近同事推荐了款 `Markdown` 编辑器,感觉比我之前用的 `MarkdownPad2`好用

分享下,名字: **[Haroopad](http://pad.haroopress.com/)**

顺便丢个自己的GitHub仓库,收集了一些开发必须的工具,需要的可以去看看,顺便 watch fuck star...

**[开发日常应该必备吧(今天发现的hosts不错,YouTube也可以看,已经提供了七牛云下载) https://github.com/didikee/Dev_Tools](https://github.com/didikee/Dev_Tools)**

##3. 我擦,真 正题...

> 思路:
>
> 想起之前看源码的时候谷歌用二进制存储`boolean`值,然后我想了想,貌似可以用`String`存一些值.
>
> 例如:"10101101": 可以解读为:`true false true false true true false true`

> 但是,"1"也是一个字符,它不限于"1",也可以是2或3甚至*或者%等等字符就可以.

**1. 定义 Value:**

**Value:**是一些`char`类型的值,代表一些含义.我简单的定义了四种:

```
public abstract class SPValue {
    public static final char TRUE='1';//true
    public static final char FALSE='0';//false
    public static final char DEFAULT='-';//默认的时候会是'-'
    public static final char ERROR='=';//发生错误的时候可能会用'='
}
```

**2. 定义 Key:**

**Key:**是由 **0 ~ Integer.MaxValue() **,其实就是`String`的脚标`index`.
```
public abstract class SPKey {
    //抽象类,给子类继承
}
```
**3. 提供 get 和 put 功能**

**定义功能接口:**
```
public interface ISPDao {
    char getValue(int key);
    void putValue(int key,char value);
}
```

**最后是实现类:**
```
public final class SPDao implements ISPDao {

    private final Context context;
    public static final String SPDAO = "spDao";
    public static final String SPKEY = "spKey";

    public SPDao(Context context) {
        this.context = context;
    }

    @Override
    public char getValue(int key) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);
        String allValues = sp.getString(SPKEY, "");
        Log.e("test","get: "+allValues);
        String defaultStr = String.valueOf(SPValue.DEFAULT);
        int maxIndex = allValues.length() - 1;
        if (key > maxIndex) {
            SharedPreferences.Editor editor = sp.edit();
            String tempAdd = "";
            for (int i = maxIndex + 1; i <= key; i++) {
                tempAdd += defaultStr;
            }
            editor.putString(SPKEY, allValues + tempAdd);
            editor.apply();
            return SPValue.DEFAULT;
        } else {
            return allValues.charAt(key);
        }
    }

    @Override
    public void putValue(int key, char value) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);
        String allValues = sp.getString(SPKEY, "");
        Log.e("test","put start: "+allValues);
        String defaultStr = String.valueOf(SPValue.DEFAULT);
        SharedPreferences.Editor editor = sp.edit();
        int maxIndex = allValues.length() - 1;
        if (key > maxIndex) {
            String tempAdd = "";
            for (int i = maxIndex + 1; i <= key; i++) {
                tempAdd += defaultStr;
            }
            Log.e("test","put end1: "+allValues+ tempAdd);
            editor.putString(SPKEY, allValues+tempAdd);
        } else {
            char oldChar = allValues.charAt(key);
            Log.e("test","oldCar: "+oldChar);
            StringBuilder sb=new StringBuilder(allValues);
            sb.setCharAt(key,value);
            Log.e("test","put end2: "+ sb.toString());
            editor.putString(SPKEY, sb.toString());
        }
        editor.apply();
    }

}
```

**说明:**中间的if作用是,如果你要获取脚标 4 的char值,但是目前String长度只有 3 ,那么我就填充String 到index 4,填充的值"DEFAULT
值.同理,我在put的时候也会做这样的处理.
最后就是Key的值其实是0开始,建议是连续的,因为不是连续的话,那么必然会有一些index的占位是无意义的,至于我最开始的思路是相违背的.

**4.最后提供一个检测 key 是否重复定义的工具类**
思路: 用反射得到所有的定义的key,然后比较是否重复,重复的话意味着同一个脚标index同时代表了两种含义,这是不被允许的.
代码:
```
public class SPDaoChecker {

    private static final String CHANGE="$change";
    private static final String SERIAL_VERSION_UID="serialVersionUID";

    /**
     * check your SPKeys
     * @param clz class extend SPKey
     * @return
     */
    public static boolean check(Class clz){
        ArrayList<Integer> charList=new ArrayList();
        Field[] fields = clz.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (SERIAL_VERSION_UID.equalsIgnoreCase(name)
                    || CHANGE.equalsIgnoreCase(name)){
                continue;
            }
            boolean accessible = field.isAccessible();
            if (!accessible)field.setAccessible(true);
            int value= -1;
            try {
                value = field.getInt(clz.newInstance());
            } catch (Exception e){
                Log.d("test","e: "+e.getClass().getSimpleName()+"msg: "+e.getMessage());
                return false;
            }
            if (charList.contains(value)){
                return false;
            }else {
                charList.add(value);
            }
            Log.d("test","name: "+name+"value: "+value);

        }
        return true;
    }
}
```

好了,差不多说完了,有什么不对的地方轻拍,你也不会打我的对不对?嘿嘿

代码地址:
(欢迎fuck 我
的代码)
[https://github.com/didikee/SharedPreferencesDao](https://github.com/didikee/SharedPreferencesDao)
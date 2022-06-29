package com.github.code.zxs.resource.service.manager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptManager {

    public static final String SCRIPT_DIRECT = "script";
    public static final String SCRIPT_SUFFIX = ".lua";

    private static final Map<String, DefaultRedisScript> map = new HashMap<>();

    static {
        //读取resource/script目录下的脚本文件
        String scriptStr, returnType = null, filename = null;
        try {
            File direct = new ClassPathResource(SCRIPT_DIRECT).getFile();
            String[] scriptList = direct.list((dir, name) -> name.endsWith(SCRIPT_SUFFIX));
            if (scriptList != null && scriptList.length != 0) {
                for (String name : scriptList) {
                    filename = SCRIPT_DIRECT + "/" + name;
                    DefaultRedisScript script = new DefaultRedisScript<>();
                    ResourceScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource(filename));
                    script.setScriptSource(scriptSource);
                    scriptStr = scriptSource.getScriptAsString();
                    returnType = scriptStr.substring(scriptStr.indexOf("--") + 2, scriptStr.indexOf('\n')).trim();
                    script.setResultType(Class.forName(returnType));
                    String key = name.substring(0, name.indexOf('.'));
                    map.put(key, script);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("读取脚本文件" + filename + "失败", e);
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("脚本文件" + filename + "返回类型格式错误", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("脚本返回类型 " + returnType + " 未找到", e);
        }
    }

    public static <T> DefaultRedisScript<T> getScript(String name) {
        return map.get(name);
    }
}

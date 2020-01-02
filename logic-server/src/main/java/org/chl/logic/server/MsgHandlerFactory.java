package org.chl.logic.server;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.chl.common.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgHandlerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(MsgHandlerFactory.class);
    private static final String HANDLER_PKG = "org.chl";
    private static final String PROTOCOL_CFG_FILE = "logic-server/src/main/resources/proto.json";
    private static final Map<Integer, Ihandler> MSG_HANLERS = new HashMap<>();
    private static JSONObject protoMeesage;
    private static List<Class<?>> listClass = null;

    private static Class<?> getHandler(String className) {
        Class<?> handler = null;
        for (Class<?> tmp : listClass) {
            if (tmp.getName().contains(className)) {
                handler = tmp;
                break;
            }
        }
        listClass.remove(handler);
        return handler;
    }

    public static void loadConfig() {
        String jsonStr = null;
        try {
            jsonStr = FileUtils.readFileToString(new File(PROTOCOL_CFG_FILE), "UTF-8");
            protoMeesage = JSONObject.parseObject(jsonStr);
            listClass = PackageUtil.getAllAssignedClass(Ihandler.class, HANDLER_PKG);
            for (String key : protoMeesage.keySet()) {
                if (key.contains(".Req")) {
                    Integer msgId = protoMeesage.getJSONObject(key).getIntValue("msgId");
                    String[] keyArray = key.split("\\.");
                    String className = keyArray[1] + "Handler";
                    Class<?> ih = getHandler(className);
                    if (ih == null) {
                        LOG.error("协议[" + msgId + "]" + className + "找不到实现类");
                        continue;
                    }
                    Ihandler handler = (Ihandler) ih.getDeclaredConstructor().newInstance();
                    MSG_HANLERS.put(msgId, handler);
                }
            }
        } catch (Exception e) {
            LOG.error("cuowu",e);
            LOG.error("加载协议配置文件[" + PROTOCOL_CFG_FILE + "][{}]错误", jsonStr);
        }
    }

    /**
     * 获取消息处理handler
     * @param id
     * @return
     */
    public static final Ihandler getHandler(int id) {
        return MSG_HANLERS.get(id);
    }

    /**
     * 根据字符串获取模块id
     * @param moduleKey
     * @return
     */
    public static final int getModuleId(String moduleKey) {
        int moduleId = 0;
        if(protoMeesage.containsKey(moduleKey)) {
            moduleId = protoMeesage.getJSONObject(moduleKey).getIntValue("id");
        }else {
            LOG.error("加载[" + moduleKey + "]错误");
        }
        return moduleId;
    }

	public static int getProtocol(String msgKey) {
		int protocol = 0;
		if(protoMeesage.containsKey(msgKey)) {
			protocol = protoMeesage.getJSONObject(msgKey).getIntValue("id");
		}else {
			LOG.error("加载[" + msgKey + "]错误");
		}
		return protocol;
	}
}

package com.rainchain.arclight.utils;


import com.alibaba.fastjson2.JSON;
import com.rainchain.arclight.entity.AuditResult;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.KpService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TextModerationUtils {

    private static final String secretId = "";
    private static final String secretKey = "";
    @SuppressWarnings("all")
    public static AuditResult textModerate(String str) throws TencentCloudSDKException {
        Credential cred = new Credential(secretId, secretKey);
        TmsClient client = new TmsClient(cred, "ap-shanghai");
        TextModerationRequest req = new TextModerationRequest();
        //设置审核内容（普通b64编码）
        req.setContent(Base64Utils.convertToNormalBase64(str));
        TextModerationResponse resp = client.TextModeration(req);
        String jsonStr = TextModerationResponse.toJsonString(resp);
        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parseObject(jsonStr);
        AuditResult auditResult = new AuditResult();
        auditResult.setLabel((String) jsonMap.get("Label"));
        auditResult.setScore((Integer) jsonMap.get("Score"));
        List<String> keywordsList = (List<String>) jsonMap.get("Keywords");
        StringBuilder keywords = new StringBuilder();
        for (String keyword : keywordsList) {
            keywords.append(keyword);
        }
        auditResult.setKeywords(keywords.toString());
        return auditResult;
    }

    public static void auditText(Game game, KpService kpService) throws TencentCloudSDKException {
        Map<String, String> info = new HashMap<String, String>() {{
            put("Ad", "广告");
            put("Porn", "色情");
            put("Abuse", "谩骂");
        }};
        AuditResult auditResult = TextModerationUtils.textModerate(game.getDes());

        if (!auditResult.getLabel().equals("Normal") && auditResult.getScore() > 70) {
            auditResult.setTitle(game.getTitle());
            auditResult.setKp_qq(game.getKp_qq());
            auditResult.setDes(game.getDes());
            kpService.addIrregularGame(auditResult);
            throw new OperationFailException("添加失败！存在疑似" + info.get(auditResult.getLabel()) + "不良信息");
        }
    }
}

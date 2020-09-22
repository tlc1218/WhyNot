import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gpdi4th.VehicleApplication;
import com.gpdi4th._framework.consts.ScanConst;
import com.gpdi4th._framework.util.*;
import com.gpdi4th._framework.util.wechat.AppUtil;
import com.gpdi4th._framework.util.wechat.FaceUtil;
import com.gpdi4th.entity.system.SysDictDetail;
import com.gpdi4th.service.disease.IDisOptionService;
import com.gpdi4th.service.system.ISysDictDetailService;
import com.gpdi4th.service.vehicle.IVeCompanyUserService;
import com.gpdi4th.service.vehicle.IVeHealthInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = VehicleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnitTest {

    @Autowired
    AppUtil appUtil;
    @Autowired
    IDisOptionService iDisOptionService;
    @Autowired
    IVeHealthInfoService iVeHealthInfoService;
    @Autowired
    IVeCompanyUserService iVeCompanyUserService;
    @Autowired
    FaceUtil faceUtil;
    @Autowired
    ISysDictDetailService iSysDictDetailService;
    @Autowired
    RequestUtil requestUtil;


    @Test
    public void getRsaKey() throws Exception {
        Map<String, String> keys = RSAUtil.initKey();
        System.out.println("PublicKey:" + keys.get("publicKey"));
        System.out.println("PrivateKey:" + keys.get("privateKey"));
    }

    @Test
    public void getBCryptStr() {
        System.out.println(BCryptUtil.encrypt("zjga@0759"));
    }

    @Test
    public void dateTest() throws Exception {
        String a = "uTCBuN8GT8ABFR1mMA0RrtkCH5YGNop1fPibheqdS3G5MJTEV+WTP6aYJydk02/C/JTBBSHEDiAaxkun3cFkgmo6S1SEKsnZtJK9Ewz6UR7uQHq1yII2oD8NZPoP6IH1wctHiw56f5jPu6I1B1ya5ICQCS6dpKIQoBa7m4fMrZSHSIfWzcwhgp5qdZmcnfHe+New8+p/rgQmkI21lDnGgS8J5bSbYELqhaKaFPazQaxNq08PZTeoh1+Ez0WmlFycCS+syVGZh9oY4LYBUqPT+5zVkemBnuhPx4LKMy4NeMuLCm/2z5h5ulq23HLnhU9B0CQMCmlPj8+j0k9i2L66NtGGSKw+h6Hdz0zPsRBTfhk4hi0dPmKD08vu2fW865WB8jSz39wt1YKDpRs4RjjlCW+YEDlSkPyyQjouElOrpmQKlLyu2LSG3XWmDDpRzNpAkYCY+Z6goijANBjDGmbOexuWtbyV0LwoWtjD7REzsaoBtr+NHAcrC+5RHiM9v4OldbYspFaGwRU9GzJQOu+Xr27yYgHoTYKmi+WKebegcLZfptbTUGb7tET7ChNTCKkptNrUZJnkQfJg63cdw2+RV4NiaDWTbNqeYgSbcxJU8sU=";
        String b = AESCbcUtil.decrypt(a, AESCbcUtil.KEY, AESCbcUtil.IV);
        System.out.println(b);
//        String s = "encode=IhMwN5Q5x8Wtg1lwlhrnqykoFkLu9uaHvs/YgP1QMVI=&unixTimeStamp=1585709990&secretKey=203b5136a40d4e1c";
//        System.out.println(Sha256Util.toSHA256(s));
    }

    @Test
    public void t() throws Exception {
        String url = "https://zjjkm.gdzj110.gov.cn/vehicle/openApi/getHealthInfo";


        String code = AESCbcUtil.encrypt(ScanConst.health + "," + 3077 + "," + new Date().getTime(), AESCbcUtil.KEY, AESCbcUtil.IV);
        String secretKey = "203b5136a40d4e1c";
        Map<String, Object> param = new HashMap<>();
        param.put("encode", code);
        Long stamp = System.currentTimeMillis() / 1000 - 22;
        param.put("unixTimeStamp", stamp.toString());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : param.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        sb.append("secretKey=");
        sb.append(secretKey);
        param.put("signature", Sha256Util.toSHA256(sb.toString()));
        JSONObject jsonObject = requestUtil.doPost(url, param, null);
        System.out.println(jsonObject);

        String b = AESCbcUtil.decrypt(jsonObject.getString("data"), AESCbcUtil.KEY, AESCbcUtil.IV);
        System.out.println(b);
        JSONObject json = JSONObject.parseObject(b);
        System.out.println(json);
    }

    @Autowired
    TencentAiUtil tencentAiUtil;

    @Test
    public void t1() throws Exception {
        for (int i = 0; i < 20; i++) {
            String verifyResult = "XXIzTtMqCxwOaawoE91-VMUGERsbiUBGjRFI1iJh2a5k30bhKlx23F48hmlky82IWsrjyR_Mh_8p_rOTdFgyVTo6afJzf9HQPZpbfkEawmBi0rADbF_GordvrnEvq_hF";
            JSONObject jsonObject = faceUtil.faceCheck(verifyResult);
            System.out.println(jsonObject);
            System.out.println();
        }
//        File file = new File("F:\\1.jpg");
//        tencentAiUtil.ocrCardIdFront(file);
    }

    @Test
    public void t2() throws Exception {
        String jsonFilePath = "C:\\Users\\12642\\Desktop\\other.json";
        File file = new File(jsonFilePath);
        String input = FileUtils.readFileToString(file, "UTF-8");
        JSONObject obj = JSONObject.parseObject(input);
        JSONArray array = obj.getJSONArray("RECORDS");

        Set<Set<SysDictDetail>> veDetailsSet = new HashSet<>();
        Set<SysDictDetail> veDetails = new HashSet<>();
        for (int i = 1; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            SysDictDetail sysDictDetail = new SysDictDetail();
            sysDictDetail.setTypeId(17);
            sysDictDetail.setName(jsonObject.getString("cname"));
            sysDictDetail.setEnglishName(jsonObject.getString("name"));

            veDetails.add(sysDictDetail);

            //4000一次批量
            if (i % 4000 == 0) {
                veDetailsSet.add(veDetails);
                veDetails = new HashSet<>();

            }
        }

        //添加多出来的数据
        if (!veDetails.isEmpty()) {
            veDetailsSet.add(veDetails);
        }

        for (Set<SysDictDetail> sysDictDetailSet : veDetailsSet) {
            iSysDictDetailService.batchInsert(sysDictDetailSet);
        }

    }

    @Test
    public void outputExcel() throws Exception {
         Thread.sleep(2000);
        String appkey = "5GLMF109GN87EYSO";
        LinkedHashMap<String,Object> userinfo = new LinkedHashMap<>();
        userinfo.put("name","余杜铭");
        userinfo.put("mobile","18875985121");
        userinfo.put("cid_type","1000");
        userinfo.put("cid","440803198412072942");

        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("city_code","4408");

        ;LinkedHashMap<String,Object> paramMap = new LinkedHashMap<>();
        paramMap.put("userinfo",AESECBUtil.encrypt(JSONObject.toJSONString(userinfo),appkey));
        paramMap.put("data",dataMap);

        String yssSign = Sha256Util.toSHA256(JSONObject.toJSONString(paramMap, SerializerFeature.WriteMapNullValue) + appkey);
        String yssUrl = "https://zwms.gdbs.gov.cn/ebus/minshengwxmp/api/r/openproxy/tprreport/GetYueKangMa?appid=ZJJKM_4VCSWCVM8C3DX825&signature=" + yssSign ;
        JSONObject result = requestUtil.doPost(yssUrl,paramMap, getHeader());
        if (result.getInteger("errcode") != 0){
            log.info(result.getString("errmsg"));
        }

        JSONObject record = (JSONObject.parseObject(result.getJSONObject("data").getString("record")));
        if (record.getInteger("errcode") != 0){
            log.info(record.getString("errmsg"));
        }

        JSONObject data = record.getJSONObject("data");
        JSONArray keys = data.getJSONArray("keys");
        boolean red = false;
        for (Object key : keys) {
            JSONObject item = data.getJSONObject( (String) key);
            if (item.getInteger("value") == 1) {
                System.out.println(item.getString("label"));
                red = true;
                break;
            }
        }
    }

    class HealthThread  implements Callable<Object>  {
        private Integer count;
        public HealthThread(Integer count) {
            this.count = count;
        }

        @Override
        public Map<String, Object> call()  {
            List<Map<String, Object>> healthList = iVeHealthInfoService.getList(count*800);
            List<Map<String, Object>> greenList = new ArrayList<>();
            List<Map<String, Object>> redList = new ArrayList<>();
            List<Map<String, Object>> exceptionList = new ArrayList<>();
            for (Map<String, Object> map : healthList) {
                Boolean red = false;
                try {
                    red = getCode(map);
                } catch (Exception e) {
                    exceptionList.add(map);
                    continue;
                }
                if (red) {
                    redList.add(map);
                } else {
                    greenList.add(map);
                }
            }
            Map<String,Object> result = new HashMap<>();
            result.put("redList",redList);
            result.put("greenList",greenList);
            result.put("exceptionList",exceptionList);
            if (!redList.isEmpty()){
            iVeHealthInfoService.insertRed(redList);
            }
            if (!exceptionList.isEmpty()){
                iVeHealthInfoService.insertException(exceptionList);
            }
            return result;
        }
    }

    @Test
    public void pkcs7Padding() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(60);
        List<Future> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Callable<Object> c = new HealthThread(i);
            Future f = pool.submit(c);
            list.add(f);
        }
        pool.shutdown();

        List<Map<String, Object>> greenList = new ArrayList<>();
        List<Map<String, Object>> redList = new ArrayList<>();
        List<Map<String, Object>> exceptionList = new ArrayList<>();
        for (Future f : list) {
            greenList.addAll( (List<Map<String,Object>>)((Map<String, Object>) f.get()).get("greenList"));
            redList.addAll( (List<Map<String,Object>>)((Map<String, Object>) f.get()).get("redList"));
            exceptionList.addAll( (List<Map<String,Object>>)((Map<String, Object>) f.get()).get("exceptionList"));
        }
        System.out.println(greenList.size());
        System.out.println(redList.size());
        System.out.println(exceptionList.size());

    }

    private Boolean getCode(Map<String, Object> map) throws Exception {

        String appkey = "5GLMF109GN87EYSO";

        LinkedHashMap<String, Object> userinfo = new LinkedHashMap<>();
        userinfo.put("name", map.get("name"));
        userinfo.put("mobile", map.get("phone"));
        userinfo.put("cid_type", "1000");
        userinfo.put("cid", map.get("cardId"));

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("city_code", "4408");

        ;
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("userinfo", AESECBUtil.encrypt(JSONObject.toJSONString(userinfo), appkey));
        paramMap.put("data", dataMap);

        String yssSign = Sha256Util.toSHA256(JSONObject.toJSONString(paramMap, SerializerFeature.WriteMapNullValue) + appkey);
        String yssUrl = "https://zwms.gdbs.gov.cn/ebus/minshengwxmp/api/r/openproxy/tprreport/GetYueKangMa?appid=ZJJKM_4VCSWCVM8C3DX825&signature=" + yssSign;
        JSONObject result = requestUtil.doPost(yssUrl, paramMap, getHeader());
        if (result.getInteger("errcode") != 0) {
            throw new Exception();
        }

        JSONObject record = (JSONObject.parseObject(result.getJSONObject("data").getString("record")));
        if (record.getInteger("errcode") != 0) {
            throw new Exception();
        }

        JSONObject data = record.getJSONObject("data");
        JSONArray keys = data.getJSONArray("keys");
        boolean red = false;
        for (Object key : keys) {
            JSONObject item = data.getJSONObject((String) key);
            if (item.getInteger("value") == 1) {
                red = true;
                break;
            }
        }
        return red;
    }

    private Map<String, String> getHeader() throws Exception {
        Long timestamp = System.currentTimeMillis() / 1000;
        String pass_token = "KPwT06yV3lR4mhHhiDX0b3sVcQWURGOf";
        String nonce = RandomStringUtils.randomAlphanumeric(16);
        String signature = Sha256Util.toSHA256(timestamp + pass_token + nonce + timestamp).toUpperCase();
        Map<String, String> header = new HashMap<>();
        header.put("X-Tif-Paasid", "yss_zjjiankangma");
        header.put("X-Tif-Signature", signature);
        header.put("X-Tif-Timestamp", timestamp.toString());
        header.put("X-Tif-Nonce", nonce);
        return header;
    }

}

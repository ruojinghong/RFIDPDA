package com.bigoffs.rfid.mvp.bean.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/29 17:27
 */
@Entity(indexes = {
        @Index(value = "rfidCode DESC",unique =  false)
})
public class AllcationCase {
        @Id(autoincrement = true)
        private Long id;
        private String boxCode;
        private boolean sign;
        private String incode;
        private String rfidCode;
        //是否已经被扫描过了
        private boolean isScan;
        @Generated(hash = 744299946)
        public AllcationCase(Long id, String boxCode, boolean sign, String incode,
                String rfidCode, boolean isScan) {
            this.id = id;
            this.boxCode = boxCode;
            this.sign = sign;
            this.incode = incode;
            this.rfidCode = rfidCode;
            this.isScan = isScan;
        }
        @Generated(hash = 1629640373)
        public AllcationCase() {
        }
        public String getBoxCode() {
            return this.boxCode;
        }
        public void setBoxCode(String boxCode) {
            this.boxCode = boxCode;
        }
        public boolean getSign() {
            return this.sign;
        }
        public void setSign(boolean sign) {
            this.sign = sign;
        }
        public String getIncode() {
            return this.incode;
        }
        public void setIncode(String incode) {
            this.incode = incode;
        }
        public String getRfidCode() {
            return this.rfidCode;
        }
        public void setRfidCode(String rfidCode) {
            this.rfidCode = rfidCode;
        }
        public String getIndexName(String name){
            return "incode";
        }
        public Long getId() {
            return this.id;
        }
      
        public boolean getIsScan() {
            return this.isScan;
        }
        public void setIsScan(boolean isScan) {
            this.isScan = isScan;
        }
        public void setId(Long id) {
            this.id = id;
        }

}

package com.android.lvxin.model;

import com.android.lvxin.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 方案详细信息
 */
public class GetPlanDetailAck extends AutoJsonAck {
    public PlanDetailInfo data;

    public PlanDetailInfo getData() {
        return data;
    }

    public void setData(PlanDetailInfo data) {
        this.data = data;
    }

    /**
     *
     */
    public static class PlanDetailInfo implements Serializable {

        private int id;
        private String title;
        private String subTitle;
        private String shortDescription;
        private int peopleNum;
        private int moveTime;
        private int actionsNum;
        private int heartrateZonesUpper;
        private int heartrateZonesLower;
        private String planInstruct;
        private String usingBraceletBenefits;
        private String attention;
        private String bgPicUrl;
        private String bgMusicUrl;
        private String audioVideoZiplink;
        private String createTime;
        private String modifyTime;
        private String moreHighheartrateVideoFn;
        private String lessLowheartrateVideoFn;
        private String firstLowheartrateVideoFn;
        private String lessLowheartrateVideoTime;
        private int isSubscribe;
        private int planCount;
        private int planSecond;
        private int warningHours;
        private int warningMinutes;
        private int warningSwitch;
        private List<PlanVideoInfo> planVideoList;
        private List<UserList> userPlanList;
        private String bannerPicUrl;
        private int coast; // 付费类型：0：免费，1：会员，2：已购买

        public int getCoast() {
            return coast;
        }

        public void setCoast(int coast) {
            this.coast = coast;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        public int getMoveTime() {
            return moveTime;
        }

        public void setMoveTime(int moveTime) {
            this.moveTime = moveTime;
        }

        public int getActionsNum() {
            return actionsNum;
        }

        public void setActionsNum(int actionsNum) {
            this.actionsNum = actionsNum;
        }

        public int getHeartrateZonesUpper() {
            return heartrateZonesUpper;
        }

        public void setHeartrateZonesUpper(int heartrateZonesUpper) {
            this.heartrateZonesUpper = heartrateZonesUpper;
        }

        public int getHeartrateZonesLower() {
            return heartrateZonesLower;
        }

        public void setHeartrateZonesLower(int heartrateZonesLower) {
            this.heartrateZonesLower = heartrateZonesLower;
        }

        public String getPlanInstruct() {
            return planInstruct;
        }

        public void setPlanInstruct(String planInstruct) {
            this.planInstruct = planInstruct;
        }

        public String getUsingBraceletBenefits() {
            return usingBraceletBenefits;
        }

        public void setUsingBraceletBenefits(String usingBraceletBenefits) {
            this.usingBraceletBenefits = usingBraceletBenefits;
        }

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public String getBgPicUrl() {
            return bgPicUrl;
        }

        public void setBgPicUrl(String bgPicUrl) {
            this.bgPicUrl = bgPicUrl;
        }

        public String getBgMusicUrl() {
            return bgMusicUrl;
        }

        public void setBgMusicUrl(String bgMusicUrl) {
            this.bgMusicUrl = bgMusicUrl;
        }

        public String getAudioVideoZiplink() {
            return audioVideoZiplink;
        }

        public void setAudioVideoZiplink(String audioVideoZiplink) {
            this.audioVideoZiplink = audioVideoZiplink;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getMoreHighheartrateVideoFn() {
            return moreHighheartrateVideoFn;
        }

        public void setMoreHighheartrateVideoFn(String moreHighheartrateVideoFn) {
            this.moreHighheartrateVideoFn = moreHighheartrateVideoFn;
        }

        public String getLessLowheartrateVideoFn() {
            return lessLowheartrateVideoFn;
        }

        public void setLessLowheartrateVideoFn(String lessLowheartrateVideoFn) {
            this.lessLowheartrateVideoFn = lessLowheartrateVideoFn;
        }

        public String getFirstLowheartrateVideoFn() {
            return firstLowheartrateVideoFn;
        }

        public void setFirstLowheartrateVideoFn(String firstLowheartrateVideoFn) {
            this.firstLowheartrateVideoFn = firstLowheartrateVideoFn;
        }

        public String getLessLowheartrateVideoTime() {
            return lessLowheartrateVideoTime;
        }

        public void setLessLowheartrateVideoTime(String lessLowheartrateVideoTime) {
            this.lessLowheartrateVideoTime = lessLowheartrateVideoTime;
        }

        public int getIsSubscribe() {
            return isSubscribe;
        }

        public void setIsSubscribe(int isSubscribe) {
            this.isSubscribe = isSubscribe;
        }

        public int getPlanCount() {
            return planCount;
        }

        public void setPlanCount(int planCount) {
            this.planCount = planCount;
        }

        public int getPlanSecond() {
            return planSecond;
        }

        public void setPlanSecond(int planSecond) {
            this.planSecond = planSecond;
        }

        public List<PlanVideoInfo> getPlanVideoList() {
            return planVideoList;
        }

        public void setPlanVideoList(List<PlanVideoInfo> planVideoList) {
            this.planVideoList = planVideoList;
        }

        public List<UserList> getUserPlanList() {
            return userPlanList;
        }

        public void setUserPlanList(List<UserList> userPlanList) {
            this.userPlanList = userPlanList;
        }

        public int getWarningHours() {
            return warningHours;
        }

        public void setWarningHours(int warningHours) {
            this.warningHours = warningHours;
        }

        public int getWarningMinutes() {
            return warningMinutes;
        }

        public void setWarningMinutes(int warningMinutes) {
            this.warningMinutes = warningMinutes;
        }

        public boolean isSwitchOn() {
            return 1 == warningSwitch;
        }

        public void setWarningSwitch(int warningSwitch) {
            this.warningSwitch = warningSwitch;
        }

        public String getBannerPicUrl() {
            return bannerPicUrl;
        }

        public void setBannerPicUrl(String bannerPicUrl) {
            this.bannerPicUrl = bannerPicUrl;
        }
    }

    /**
     *
     */
    public static class PlanVideoInfo implements Serializable {

        private int id;
        private int planId; // 调理计划id
        private String planVideoName; // 调理计划标题
        private String pictureUrl; // 图片URL
        private int movementTime; // 动作时间
        private String videoUrl; // 视频URL
        private int playSort; // 播放排序
        private String createTime; // 创建时间
        private String modifyTime; // 修改时间
        private int playTimes; // 播放次数
        private int introPlayTimes; // 播放次数
        private int guideType; // 指导类型 1:按次数 2:按时间
        private int restTime; // 休息时间
        private String unitList; // 动作音频名称列表
        private String introList; // 开场音频列表
        private String detailIntroList; // 其他音频列表: 请看示范语音,一组n次/一组n秒语音, 3 2 1倒计时语音
        private String unitName; // unit名称
        private String introName; // intro名称
        private List<String> unitAudios;
        private List<String> introAudios;

        private List<String> otherAudios;
        private String actionDesc;

        public PlanVideoInfo(String name, String introName, String unitName, int during, int repeats,
                             int type, int restTime, String unitList) {
            this.planVideoName = name;
            this.unitName = unitName;
            this.introName = introName;
            this.movementTime = during;
            this.playTimes = repeats;
            this.guideType = type;
            this.restTime = restTime;
            setUnitList(unitList);

        }

        public String getDetailIntroList() {
            return detailIntroList;
        }

        public void setDetailIntroList(String detailIntroList) {
            this.detailIntroList = detailIntroList;
        }

        public List<String> getUnitAudios() {
            if (!StringUtils.isEmpty(unitList)) {
                this.unitAudios = parseAudioString(unitList);
            } else {
                unitAudios = new ArrayList<>(0);
            }
            return unitAudios;
        }

        public List<String> getIntroAudios() {
            if (!StringUtils.isEmpty(introList)) {
                this.introAudios = parseAudioString(introList);

            } else {
                introAudios = new ArrayList<>(0);
            }
            return introAudios;
        }

        public List<String> getOtherAudios() {
            if (!StringUtils.isEmpty(detailIntroList)) {
                this.otherAudios = parseAudioString(detailIntroList);
            } else {
                otherAudios = new ArrayList<>(0);
            }
            return otherAudios;
        }

        public int getIntroPlayTimes() {
            return introPlayTimes;
        }

        public void setIntroPlayTimes(int introPlayTimes) {
            this.introPlayTimes = introPlayTimes;
        }

        public String getIntroName() {
            return introName;
        }

        public void setIntroName(String introName) {
            this.introName = introName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlanId() {
            return planId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public String getPlanVideoName() {
            return planVideoName;
        }

        public void setPlanVideoName(String planVideoName) {
            this.planVideoName = planVideoName;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getMovementTime() {
            return movementTime;
        }

        public void setMovementTime(int movementTime) {
            this.movementTime = movementTime;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getPlaySort() {
            return playSort;
        }

        public void setPlaySort(int playSort) {
            this.playSort = playSort;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public int getPlayTimes() {
            return playTimes;
        }

        public void setPlayTimes(int playTimes) {
            this.playTimes = playTimes;
        }

        public int getGuideType() {
            return guideType;
        }

        public void setGuideType(int guideType) {
            this.guideType = guideType;
        }

        public int getRestTime() {
            return restTime;
        }

        public void setRestTime(int restTime) {
            this.restTime = restTime;
        }

        public String getUnitList() {
            return unitList;
        }

        public void setUnitList(String unitList) {
            this.unitList = unitList;
        }

        public String getIntroList() {
            return introList;
        }

        public void setIntroList(String introList) {
            this.introList = introList;
        }


        public String getActionDesc() {
            return actionDesc;
        }

        public void setActionDesc(String actionDesc) {
            this.actionDesc = actionDesc;
        }

        /**
         * 解析音频列表
         * 服务端返回的是以逗号隔开的字符串，遇到重复音以"文件名*次数"的格式返回
         * e.g. audio_file_name_1,audio_file_name_2*3,audio_file_name_3,...
         *
         * @param audioString
         * @return
         */
        private List<String> parseAudioString(String audioString) {
            if (StringUtils.isEmpty(audioString)) {
                return new ArrayList<>(0);
            }
            String[] values = audioString.split(",");
            List<String> result = new ArrayList<>();
            for (String value : values) {
                if (!StringUtils.isEmpty(value)) {
                    int index = value.lastIndexOf("*");
                    if (-1 != index) {
                        if (index < value.length() - 1) {
                            String audioName = value.substring(0, index);
                            int repeat = Integer
                                    .parseInt(value.substring(index + 1, value.length()));
                            for (int i = 0; i < repeat; i++) {
                                result.add(audioName);
                            }
                        }
                    } else {
                        result.add(value);
                    }
                }
            }
            return result;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

    }

    /**
     *
     */
    public static class UserList implements Serializable {
        private int userId;
        private String memberIcon;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getMemberIcon() {
            return memberIcon;
        }

        public void setMemberIcon(String memberIcon) {
            this.memberIcon = memberIcon;
        }
    }
}

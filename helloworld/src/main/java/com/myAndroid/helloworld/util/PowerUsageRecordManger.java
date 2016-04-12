package com.myAndroid.helloworld.util;

/**
 * @author ZhouXinXing
 * @date 2016年02月24日 18:43
 * @Description 指定统计时间段内的app电池用量百分比数据管理类。
 */
public enum PowerUsageRecordManger {
    getInstance;

//    private final boolean DEBUG = true;
//    private final String TAG = "PowerUsageRecordManger";
//
//    public static final String ACTION_GET_POWER_USAGE_IN_PERIOD = "PowerUsageInPeriodAction";
//
//    public static final float NOT_VALID_USAGE = -999.99F;//无效用量数据
//    public static final int NOT_VALID_USAGE_RATE = -999;//无效比率数据
//
//    public static final String MAP_KEY_SYS_CPU_USAGE = "sys_cpu_usage";//map中用以映射系统总cpu耗量的key值
//
//    private static final String APP_POWER_USAGE_FILE_NAME = "app_power_usage.txt";//本地记录电池耗量的文件名
//
//    /**
//     * 在刷新数据时，提供上锁功能。
//     * 由于刷新时候是异步回调，不在同一个线程内，所以需要使用一个线程来进行上锁、解锁的操作
//     */
//    private Handler lockHandler;
//
//    /**
//     * json映射
//     */
//    private static final String JSON_KEY_PKG = "appPkg";
//    private static final String JSON_KEY_POWER_USAGER_INFOS = "powerUsageInfos";
//    private static final String JSON_KEY_USAGER = "usages";
//    private static final String JSON_KEY_FOREGROUND_USAGE = "fgUsage";
//    private static final String JSON_KEY_TIME = "time";
//    private static final String JSON_KEY_BOOT_GROUP = "bootGroup";
//
//    public static final int POWER_USAGE_PERIOD = 24 * 60;//app电池用量的统计时间段（单位：分钟）
//    private static final int REFRESH_DELAY = 4;//更新app电量耗时数据的时间间隔（单位：分钟）
//    private static final int WRITE_DELAY = 7;//持久化app电量耗时数据的时间间隔（单位：分钟）
//
//    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(2);
//    private Future<?> refreshFuture;
//    private Future<?> saveFuture;
//
//    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();//读取锁
//    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();//写入锁
//    private final Map<String, List<PowerUsageRecord>> appPowerUsageMap = new HashMap<String, List<PowerUsageRecord>>();
//
//    private final GetTopPackageUtil mGetTopPackageUtil = new GetTopPackageUtil(BatteryPlusApplication.getAppContext());//获取栈顶app
//
//    /**
//     * 电池用量记录
//     */
//    private static final class PowerUsageRecord {
//        private final float usage;//电池用量
//        private final float fgUsage;//前台电池用量
//        private final long time;//记录时间
//        private final int bootGroup;//开机分组
//
//        public PowerUsageRecord(float usage, float foregroundUsage, long time, int bootGroup) {
//            this.usage = usage;
//            this.fgUsage = foregroundUsage;
//            this.time = time;
//            this.bootGroup = bootGroup;
//        }
//
//        @Override
//        public String toString() {
//            return "PowerUsageRecord{" + "usage=" + usage + ", fgUsage=" + fgUsage + ", time="
//                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time) + ", bootGroup=" + bootGroup + '}';
//        }
//    }
//
//    public IBinder onBind() {
//        return new IPowerUsageInPeriodBind.Stub() {
//            @Override
//            public int[] getAppPowerUsageRate(String pkg, float sysPowerUsage) throws RemoteException {
//                return PowerUsageRecordManger.getInstance.getAppPowerUsageRate(pkg, sysPowerUsage);
//            }
//
//            @Override
//            public float[] getAppPowerUsage(String pkg) throws RemoteException {
//                return PowerUsageRecordManger.getInstance.getAppPowerUsage(pkg);
//            }
//        };
//    }
//
//    /**
//     * 启动电池耗量采样任务
//     */
//    public void run() {
//        if (DEBUG) {
//            Log.d(TAG, "启动数据采样服务...");
//        }
//
//        if (appPowerUsageMap.containsKey(MAP_KEY_SYS_CPU_USAGE)) {
//            //如果含有MAP_KEY_SYS_CPU_USAGE，说明已经启动过服务，无需要再次启动
//            return;
//        }
//
//        if (lockHandler == null) {
//            HandlerThread handlerThread = new HandlerThread("power_usage_record");
//            handlerThread.start();
//            lockHandler = new Handler(handlerThread.getLooper());
//        }
//
//        if (refreshFuture != null) {
//            refreshFuture.cancel(true);
//        }
//
//        if (saveFuture != null) {
//            saveFuture.cancel(true);
//        }
//
//        if (!initPowerUsageMap()) {
//            /**
//             * 当初始化不到任何数据时，需要先将系统电池耗量的数组初始化，并立即初始化一次
//             */
//            appPowerUsageMap.put(MAP_KEY_SYS_CPU_USAGE, new ArrayList<PowerUsageRecord>());
//
//            refreshPowerUsage();
//        }
//
//        refreshFuture = schedule.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                checkBootTime();
//
//                refreshPowerUsage();
//            }
//        }, REFRESH_DELAY, REFRESH_DELAY, TimeUnit.MINUTES);
//
//        saveFuture = schedule.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                savePowerUsage();
//            }
//        }, WRITE_DELAY, WRITE_DELAY, TimeUnit.MINUTES);
//    }
//
//    /**
//     * 获取app在统计时间段内的电池使用率
//     */
//    public int[] getAppPowerUsageRate(String pkg, float sysPowerUsage) {
//        int[] powerUsageRateArray = new int[] { NOT_VALID_USAGE_RATE, NOT_VALID_USAGE_RATE };
//
//        float[] appPowerUsage = getAppPowerUsage(pkg);
//
//        if (appPowerUsage[0] == NOT_VALID_USAGE) {
//            return powerUsageRateArray;
//        }
//
//        powerUsageRateArray[0] = Math.abs((int) (appPowerUsage[0] * 100 / sysPowerUsage));//在指定统计时间段内的电池使用率
//
//        if (appPowerUsage[1] != NOT_VALID_USAGE) {
//            powerUsageRateArray[1] = Math.abs((int) ((appPowerUsage[0] - appPowerUsage[1]) * 100 / appPowerUsage[0]));//后台使用的电池比率
//        }
//
//        if (DEBUG) {
//            Log.d(TAG, pkg + " 在指定统计时间段内的电池使情况");
//            Log.d(TAG, "总共电池使用率：" + powerUsageRateArray[0] + "%");
//            Log.d(TAG, "其中" + powerUsageRateArray[1] + "% 为后台消耗");
//        }
//
//        return powerUsageRateArray;
//    }
//
//    /**
//     * 检查设备启动时间、设备启动分组
//     */
//    private void checkBootTime() {
//        if (DEBUG) {
//            Log.d(TAG, "-----checkBootTime()检查设备启动时间、设备启动分组");
//        }
//
//        Context context = BatteryPlusApplication.getAppContext();
//        long bootTime = SystemClock.elapsedRealtime();//获取当前系统返回的开机到现在的耗时
//
//        long lastBootTime = SharedPref.getLong(context, SharedPref.KEY_LAST_BOOT_TIME, 0);//获取上次缓存的开机到现在的耗时
//        if (lastBootTime == 0 || bootTime < lastBootTime) {
//            /*
//             * 如果发现当前返回的耗时时间比上次返回的小，说明设备已经重启过，需要将开机分组+1
//             */
//            bootGroupPlus(context);
//        }
//
//        if (DEBUG) {
//            Log.d(TAG, "当前启动分组为：" + SharedPref.getInt(context, SharedPref.KEY_BOOT_BROUP, 0));
//        }
//
//        SharedPref.setLong(context, SharedPref.KEY_LAST_BOOT_TIME, bootTime);//刷新开机耗时
//    }
//
//    /**
//     * 启动分组+1
//     */
//    private void bootGroupPlus(Context context) {
//        int bootGroup = SharedPref.getInt(context, SharedPref.KEY_BOOT_BROUP, 0);
//        SharedPref.setInt(context, SharedPref.KEY_BOOT_BROUP, ++bootGroup);
//    }
//
//    /**
//     * 初始化app电池用量
//     *
//     * @return 返回0时，表示appPowerUsageMap未初始化到任何数据
//     */
//    private boolean initPowerUsageMap() {
//        if (DEBUG) {
//            Log.d(TAG, "-----initPowerUsageMap()初始化app电池用量");
//        }
//
//        try {
//            writeLock("initPowerUsageMap()");
//
//            File file = new File(getFilePath() + APP_POWER_USAGE_FILE_NAME);
//
//            if (file == null || !file.exists()) {
//                if (DEBUG) {
//                    Log.d(TAG, "初始化app电池用量异常：本地电池用量文件不存在");
//                }
//
//                return false;
//            }
//
//            String jsonString = readFile(file);
//
//            if (TextUtils.isEmpty(jsonString)) {
//                if (DEBUG) {
//                    Log.d(TAG, "初始化app电池用量异常：本地电池用量文件内容为空");
//                }
//
//                return false;
//            }
//
//            if (DEBUG) {
//                Log.d(TAG, "本地文件读取成功，内容如下：\n" + jsonString);
//            }
//
//            JSONArray jsonArray = new JSONArray(jsonString);
//            if (jsonArray == null || jsonArray.length() == 0) {
//                if (DEBUG) {
//                    Log.d(TAG, "初始化app电池用量失败：本地文件解析成json对象失败");
//                }
//
//                return false;
//            }
//
//            if (jsonArray.length() <= 1) {
//                if (DEBUG) {
//                    Log.d(TAG, "初始化app电池用量失败，尚未缓存任何电量信息");
//                }
//
//                return false;
//            }
//
//            /**
//             * 解析本地json数据，组装appPowerUsageMap数据
//             */
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                String pkg = jsonObject.optString(JSON_KEY_PKG);
//                JSONArray powerUsageArray = jsonObject.optJSONArray(JSON_KEY_POWER_USAGER_INFOS);
//
//                List<PowerUsageRecord> powerUsageRecords = new ArrayList<PowerUsageRecord>();
//                if (powerUsageArray != null && powerUsageArray.length() > 0) {
//                    for (int j = 0; j < powerUsageArray.length(); j++) {
//                        JSONObject jsonObjectPowerInfo = powerUsageArray.optJSONObject(j);
//                        float usage = Float.parseFloat(jsonObjectPowerInfo.optString(JSON_KEY_USAGER));
//                        float foregroundUsage = Float
//                                .parseFloat(jsonObjectPowerInfo.optString(JSON_KEY_FOREGROUND_USAGE));
//                        long time = jsonObjectPowerInfo.optLong(JSON_KEY_TIME);
//                        int bootGroup = jsonObjectPowerInfo.optInt(JSON_KEY_BOOT_GROUP, 1);
//
//                        powerUsageRecords.add(new PowerUsageRecord(usage, foregroundUsage, time, bootGroup));
//                    }
//                }
//
//                appPowerUsageMap.put(pkg, powerUsageRecords);
//            }
//
//            if (DEBUG) {
//                Log.d(TAG, "appPowerUsageMap初始化完成，内容如下：");
//                for (String pkg : appPowerUsageMap.keySet()) {
//                    Log.d(TAG, pkg + " 耗电用量 [");
//                    for (PowerUsageRecord powerUsageRecord : appPowerUsageMap.get(pkg)) {
//                        Log.d(TAG, "  " + powerUsageRecord.toString());
//                    }
//                    Log.d(TAG, "]");
//                }
//            }
//        } catch (Exception e) {
//            if (DEBUG) {
//                Log.d(TAG, "************初始化app电池用量异常，原因：\n" + e.getLocalizedMessage());
//            }
//        } finally {
//            writeUnLock("initPowerUsageMap()");
//        }
//
//        return true;
//    }
//
//    /**
//     * 获取app在统计时间段内的耗电用量
//     *
//     * @param pkg
//     *            app包名
//     * @return int[] -> 元素1：app电池消耗量 ；元素2：app前台电池消耗量
//     */
//    public float[] getAppPowerUsage(String pkg) {
//        if (DEBUG) {
//            Log.d(TAG, "-----getAppPowerUsage()获取 " + pkg + " 在指定时间段内的耗电用量");
//        }
//
//        float[] powerUsage = new float[] { NOT_VALID_USAGE, NOT_VALID_USAGE };
//        long now = new Date().getTime();
//
//        try {
//            readLock("getAppPowerUsage()");
//
//            if (!appPowerUsageMap.containsKey(pkg) || appPowerUsageMap.get(pkg).isEmpty()) {
//                if (DEBUG) {
//                    Log.d(TAG, "未缓存 " + pkg + " 的电池用量信息，电池用量返回无效数据");
//                }
//
//                return powerUsage;
//            }
//
//            List<PowerUsageRecord> powerUsageRecords = appPowerUsageMap.get(pkg);
//
//            /**
//             * 如果只有一条数据，且同时满足以下条件，则直接返回该条数据记录的电池用量
//             * 1. 该数据的记录时间在24小时内
//             * 2. 该数据的记录时间戳小于当前时间戳
//             *
//             * (PS:此处使用do-while循环，仅仅是为了需要把多个if嵌套书写给平铺开来，而并不是为了需要实现某种循环逻辑，请读者注意。)
//             */
//            do {
//                if (appPowerUsageMap.get(pkg).size() > 1) {
//                    break;
//                }
//
//                if (now - powerUsageRecords.get(0).time > POWER_USAGE_PERIOD * 60 * 1000) {
//                    if (DEBUG) {
//                        Log.d(TAG, pkg + " 的电池用量返回无效数据（仅一条数据），记录时间在24小时之外");
//                    }
//
//                    return powerUsage;
//                }
//
//                if (powerUsageRecords.get(0).time > now) {
//                    if (DEBUG) {
//                        Log.d(TAG, pkg + " 的电池用量返回无效数据（仅一条数据），记录时间大于当前时间");
//                    }
//
//                    return powerUsage;
//                }
//
//                powerUsage[0] = appPowerUsageMap.get(pkg).get(0).usage;
//
//                if (canReturnForegroundUsage()) {
//                    powerUsage[1] = appPowerUsageMap.get(pkg).get(0).fgUsage;
//                }
//
//                if (DEBUG) {
//                    Log.d(TAG, pkg + " 的电池用量返回（仅一条数据）：" + powerUsage[0]);
//                    Log.d(TAG, pkg + " 的前台电池用量返回（仅一条数据）：" + powerUsage[1]);
//                }
//
//                return powerUsage;
//            } while (false);
//
//            /**
//             * 根据每条数据的time字段判断，截取有效的起始、结束下标位：
//             * 1. 有效的起始下标位：当前时间 - time <= 指定统计时间（当前版本需求为24小时）
//             * 2. 有效的结束下标位：time <= 当前时间
//             */
//            int startIndex = -1;
//            int endIndex = -1;
//
//            //计算有效的起始下标位
//            for (int i = 0; i < powerUsageRecords.size(); i++) {
//                if (now - powerUsageRecords.get(i).time <= POWER_USAGE_PERIOD * 60 * 1000) {
//                    startIndex = i;
//
//                    break;
//                }
//            }
//
//            if (startIndex == -1) {
//                if (DEBUG) {
//                    Log.d(TAG, "未找到有效的起始下标位，电池用量返回无效数据");
//                }
//
//                return powerUsage;
//            }
//
//            //计算有效的结束下标位
//            for (int i = powerUsageRecords.size() - 1; i >= 0; i--) {
//                if (powerUsageRecords.get(i).time <= now) {
//                    endIndex = i;
//
//                    break;
//                }
//            }
//
//            if (endIndex == -1) {
//                if (DEBUG) {
//                    Log.d(TAG, "未找到有效的结束下标位，电池用量返回无效数据");
//                }
//
//                return powerUsage;
//            }
//
//            //如果有效的起始、结束下标位相同，直接返回
//            if (startIndex == endIndex) {
//                powerUsage[0] = powerUsageRecords.get(startIndex).usage;
//
//                if (canReturnForegroundUsage()) {
//                    powerUsage[1] = powerUsageRecords.get(startIndex).fgUsage;
//                }
//
//                if (DEBUG) {
//                    Log.d(TAG, pkg + " 的电池用量返回：" + powerUsage[0]);
//                    Log.d(TAG, pkg + " 的前台电池用量返回：" + powerUsage[1]);
//                }
//
//                return powerUsage;
//            }
//
//            /**
//             * 开始计算指定时间段内app的电池用量，算法规则如下：
//             * 1. 先按照bootGroup字段将电池耗量数据分组
//             * 2. 分别计算各个分组的<末元素 减去 首元素>所得到的差值，该值即为该分组里app消耗的电池量
//             * 3. 累加所有分组里app消耗的电池量，即为该app在指定时间段内消耗的所有电池量
//             */
//            /*
//             * 按照bootGroup字段将电池耗量数据分组
//             */
//            List<List<Float>> usageGroup = new ArrayList<List<Float>>();
//            int bootGroupTemp = -1;
//            float totalForegroundUsage = 0;//app在前台总消耗的电池量
//            for (int i = startIndex; i <= endIndex; i++) {
//                PowerUsageRecord powerUsageRecord = powerUsageRecords.get(i);
//
//                totalForegroundUsage += powerUsageRecord.fgUsage;//迭代时，顺便累加一下前台耗电量
//
//                if (powerUsageRecord.bootGroup != bootGroupTemp) {
//                    //刷新分组判断阈值
//                    bootGroupTemp = powerUsageRecord.bootGroup;
//
//                    //若bootGroup不相同，则说明不是同一个开机分组，需要新建一个分组list来缓存数据
//                    List<Float> usages = new ArrayList<Float>();
//                    usages.add(powerUsageRecord.usage);
//                    usageGroup.add(usages);
//                } else {
//                    //若bootGroup相同，则复用上一次的分组list来缓存数据
//                    usageGroup.get(usageGroup.size() - 1).add(powerUsageRecord.usage);
//                }
//            }
//
//            if (usageGroup.isEmpty()) {
//                if (DEBUG) {
//                    Log.d(TAG, "电量分组为空，电池用量返回无效数据");
//                }
//
//                return powerUsage;
//            } else {
//                if (DEBUG) {
//                    Log.d(TAG, pkg + " 的电池用量分组完毕，数据如下：");
//
//                    for (List<Float> usages : usageGroup) {
//                        Log.d(TAG, "[");
//                        StringBuffer usageString = new StringBuffer();
//                        for (Float usage : usages) {
//                            usageString.append(usage + ",");
//                        }
//                        Log.d(TAG, "  " + usageString.toString());
//                        Log.d(TAG, "]");
//                    }
//                }
//            }
//
//            /*
//             * 根据分组数据，计算电池耗量
//             */
//            float totalUsage = 0;
//            for (List<Float> usages : usageGroup) {
//                if (usages.size() == 1) {
//                    //如果当前分组只有一个元素，直接返回
//                    totalUsage += usages.get(0);
//                } else {
//                    //用末元素-首元素的值，计算出该分组里app消耗的电池量
//                    totalUsage += usages.get(usages.size() - 1) - usages.get(0);
//                }
//            }
//
//            powerUsage[0] = totalUsage;
//
//            if (canReturnForegroundUsage()) {
//                powerUsage[1] = totalForegroundUsage;
//            }
//
//            if (DEBUG) {
//                Log.d(TAG, pkg + " 的电池用量返回：" + powerUsage[0]);
//                Log.d(TAG, pkg + " 的前台电池用量返回：" + powerUsage[1]);
//            }
//
//            if (powerUsage[1] > powerUsage[0]) {
//                if (DEBUG) {
//                    Log.d(TAG, pkg + " 的电池用量容错处理,防止前台电量消耗大于总电量消耗");
//                }
//
//                powerUsage[1] = powerUsage[0];
//            }
//
//            return powerUsage;
//        } finally {
//            readUnLock("getAppPowerUsage()");
//        }
//    }
//
//    /**
//     * 判断是否可以返回前台电池用量数据，条件如下：
//     * 1. 已经获取UsageAcccess权限
//     */
//    private boolean canReturnForegroundUsage() {
//        return !OpsCheckUtil.isNeedGetUsageAcccessPermission(BatteryPlusApplication.getAppContext());
//    }
//
//    /**
//     * 刷新各app电池用量
//     */
//    private void refreshPowerUsage() {
//        lockHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                writeLock("refreshPowerUsage()");
//
//                if (DEBUG) {
//                    Log.d(TAG, "-----refreshPowerUsage()刷新各app电池用量");
//                }
//
//                /**
//                 * 获取当前栈顶app包名
//                 */
//                final String topAppPkg = mGetTopPackageUtil.getTopPackageName();
//
//                if (DEBUG) {
//                    Log.d(TAG, "当前栈顶app包名：" + topAppPkg);
//                }
//
//                DataUtil.requestData(new ProcessInfoDataRequestBean(),
//                        new IDataCallBack<ProcessInfoDataResponseBean>() {
//                    long lastCallBackTime = 0;//上一次回调时间戳
//
//                    @Override
//                    public void onSuccess(ProcessInfoDataResponseBean responseBean) {
//                        List<ProcessInfo> processList = responseBean.processList;
//
//                        if (DEBUG) {
//                            Log.d(TAG, "扫描服务app列表返回");
//                        }
//
//                        // 不知为何，这里会被重复回调，暂时先用这种拙劣的方式做一层防护 -------------------
//                        long now = new Date().getTime();
//                        if (lastCallBackTime != 0 && now - lastCallBackTime < 100) {
//                            if (DEBUG) {
//                                Log.d(TAG, " <--------- 重复返回，数据无效 --------->");
//                            }
//
//                            return;
//                        }
//                        lastCallBackTime = now;
//                        // --------------------------------------------------------------------
//
//                        try {
//                            if (!processList.isEmpty()) {
//                                /**
//                                 * 刷新已缓存的app电池用量
//                                 */
//                                SharedPref.setLong(BatteryPlusApplication.getAppContext(),
//                                        SharedPref.KEY_LAST_BOOT_TIME, SystemClock.elapsedRealtime());//刷新开机到现在的耗时
//
//                                int bootGroup = SharedPref.getInt(BatteryPlusApplication.getAppContext(),
//                                        SharedPref.KEY_BOOT_BROUP, 1);
//
//                                float sysPowerUsage = 0;//系统总的电池耗量
//
//                                for (ProcessInfo processInfo : processList) {
//                                    float appPowerUsageNow = CpuUsageManager.getProcessCpuInfos(processInfo.pids);
//                                    sysPowerUsage += appPowerUsageNow;//累加系统总的电池耗量
//
//                                    /*
//                                     * 判断app是否在前台运行：如果判断为前台运行，需要将电池用量记录到前台电池用量字段中
//                                     */
//                                    float foregroundCpuUsageNow = 0;
//                                    if (processInfo.packageName.equals(topAppPkg)) {
//                                        foregroundCpuUsageNow = appPowerUsageNow;
//                                    } else {
//                                        //使用本地oom_adj文件双重校验前台运行app
//                                        for (Integer pid : processInfo.pids) {
//                                            if (CpuUsageManager.isProgressTop(pid)) {
//                                                if (DEBUG) {
//                                                    Log.d(TAG, processInfo.packageName + " 经过本地oom_adj文件校验，为前台运行应用");
//                                                }
//
//                                                foregroundCpuUsageNow = appPowerUsageNow;
//
//                                                break;
//                                            }
//                                        }
//                                    }
//
//                                    PowerUsageRecord powerUsageRecordNow;
//                                    List<PowerUsageRecord> powerUsageList;
//                                    if (!appPowerUsageMap.containsKey(processInfo.packageName)) {
//                                        /*
//                                         * 首次新增数据
//                                         */
//                                        powerUsageList = new ArrayList<PowerUsageRecord>();
//
//                                        powerUsageRecordNow = new PowerUsageRecord(appPowerUsageNow,
//                                                foregroundCpuUsageNow, now, bootGroup);
//
//                                        appPowerUsageMap.put(processInfo.packageName, powerUsageList);
//
//                                        if (DEBUG) {
//                                            Log.d(TAG, "新增 " + processInfo.packageName + " 电池用量为-> "
//                                                    + powerUsageRecordNow.toString());
//                                        }
//                                    } else {
//                                        /*
//                                         * 在原有数据集中添加数据
//                                         */
//                                        powerUsageList = appPowerUsageMap.get(processInfo.packageName);
//
//                                        //只有与上一次的电池用量同属于一个启动分组，才能够将电池用量累加(防止用量无限增大)
//                                        PowerUsageRecord lastInfo = powerUsageList.get(powerUsageList.size() - 1);
//                                        if (lastInfo.bootGroup == bootGroup) {
//                                            appPowerUsageNow += lastInfo.usage;
//                                        }
//                                        powerUsageRecordNow = new PowerUsageRecord(appPowerUsageNow,
//                                                foregroundCpuUsageNow, now, bootGroup);
//
//                                        if (DEBUG) {
//                                            Log.d(TAG, "刷新 " + processInfo.packageName + " 电池用量为-> "
//                                                    + powerUsageRecordNow.toString());
//                                        }
//                                    }
//
//                                    powerUsageList.add(powerUsageRecordNow);
//                                }
//
//                                /**
//                                 * 更新系统总的电池耗量。
//                                 * 此处与单个app的刷新规则相同，需要通过启动分组字段来判断是否需要累加上一次的电池用量
//                                 */
//                                if (!appPowerUsageMap.containsKey(MAP_KEY_SYS_CPU_USAGE)) {
//                                    if (DEBUG) {
//                                        Log.d(TAG, "系统电池用量数据丢失");
//                                    }
//                                    //容错处理，防止缓存列表中，丢失系统电量
//                                    appPowerUsageMap.put(MAP_KEY_SYS_CPU_USAGE, new ArrayList<PowerUsageRecord>());
//                                }
//
//                                List<PowerUsageRecord> sysPowerUsageRecords = appPowerUsageMap
//                                        .get(MAP_KEY_SYS_CPU_USAGE);
//
//                                if (sysPowerUsageRecords != null && !sysPowerUsageRecords.isEmpty()) {
//                                    PowerUsageRecord lastSysPowerUsageRecord = sysPowerUsageRecords
//                                            .get(sysPowerUsageRecords.size() - 1);
//                                    if (lastSysPowerUsageRecord.bootGroup == bootGroup) {
//                                        sysPowerUsage += lastSysPowerUsageRecord.usage;
//                                    }
//                                }
//
//                                PowerUsageRecord sysPowerUsageRecord = new PowerUsageRecord(sysPowerUsage, 0, now,
//                                        bootGroup);
//
//                                sysPowerUsageRecords.add(sysPowerUsageRecord);
//                                if (DEBUG) {
//                                    Log.d(TAG, "刷新系统总的电池用量为-> " + sysPowerUsageRecord.toString());
//                                }
//
//                                /**
//                                 * 为避免超出Float最大取值范围，手动将启动分组+1
//                                 */
//                                if (sysPowerUsage >= Float.MAX_VALUE * 0.8) {
//                                    if (DEBUG) {
//                                        Log.d(TAG, "即将超出Float取值范围，启动分组+1");
//                                    }
//
//                                    bootGroupPlus(BatteryPlusApplication.getAppContext());
//                                }
//                            }
//                        } catch (Exception e) {
//                            if (DEBUG) {
//                                Log.d(TAG, "刷新电池耗量数据异常，原因如下：");
//                                e.printStackTrace();
//                            }
//                        } finally {
//                            lockHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    writeUnLock("refreshPowerUsage() - onSuccess");
//
//                                    /*
//                                    * 通知卡片数据刷新
//                                    */
//                                    CardDataHelper.getInstance().changed(CardDataHelper.ChangedEnum.CHANGED_POWER_RANK);
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(DataErrorBean errorBean) {
//                        if (DEBUG) {
//                            Log.d(TAG, " *** 获取清理列表失败，原因：" + errorBean.msg);
//                        }
//
//                        lockHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                writeUnLock("refreshPowerUsage() - onFailure");
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * 持久化各app电池用量
//     */
//    private void savePowerUsage() {
//        if (DEBUG) {
//            Log.d(TAG, "-----savePowerUsage()持久化各app电池用量");
//        }
//
//        try {
//            writeLock("savePowerUsage()");
//
//            /**
//             * 持久化之前，首先删除冗余数据
//             * 1. 将各app用量列表中，时间戳已经超过统计时间的数据过滤删除（判断首元素）
//             * 2. 过滤完之后，如果该app用量列表已空，则直接删除该条app统计
//             */
//            long now = new Date().getTime();
//
//            List<String> dataEmptyApp = new ArrayList<String>();
//            for (String pkg : appPowerUsageMap.keySet()) {
//                while (true) {
//                    if (appPowerUsageMap.get(pkg).isEmpty()) {
//                        if (!pkg.equals(MAP_KEY_SYS_CPU_USAGE)) {
//                            dataEmptyApp.add(pkg);//将已空的app统计数据缓存起来，准备删除（系统统计除外）
//                        }
//
//                        break;
//                    }
//
//                    PowerUsageRecord removePowerUsageRecord = appPowerUsageMap.get(pkg).get(0);
//                    if (now - removePowerUsageRecord.time > POWER_USAGE_PERIOD * 60 * 1000) {
//                        appPowerUsageMap.get(pkg).remove(removePowerUsageRecord);
//
//                        if (DEBUG) {
//                            Log.d(TAG, "*** 删除冗余数据： pkg - " + pkg + " " + removePowerUsageRecord.toString());
//                        }
//                    } else {
//                        break;
//                    }
//                }
//            }
//
//            for (String pkg : dataEmptyApp) {
//                appPowerUsageMap.remove(pkg);
//
//                if (DEBUG) {
//                    Log.d(TAG, "删除已空的app统计数据： pkg - " + pkg);
//                }
//            }
//
//            /**
//             * 删除超载数据
//             * 如果本地文件已经大于2M，则需要清理一定量的数据(暂定清理一半数据量)
//             */
//            File file = new File(getFilePath() + APP_POWER_USAGE_FILE_NAME);
//            if (file.exists() && file.length() / 1024 / 1024 >= 2) {
//                for (String pkg : appPowerUsageMap.keySet()) {
//                    if (pkg.equals(MAP_KEY_SYS_CPU_USAGE)) {
//                        continue;
//                    }
//
//                    if (appPowerUsageMap.get(pkg).size() == 1) {
//                        continue;
//                    }
//
//                    for (int i = 0; i < appPowerUsageMap.get(pkg).size() / 2; i++) {
//                        PowerUsageRecord removePowerUsageRecord = appPowerUsageMap.get(pkg).remove(i);
//
//                        if (DEBUG) {
//                            Log.d(TAG, "+++ 删除超载数据： pkg - " + pkg + " " + removePowerUsageRecord.toString());
//                        }
//                    }
//                }
//            }
//
//            /**
//             * 组装json格式数据，并进行持久化操作
//             */
//            JSONArray jsonArray = new JSONArray();
//
//            for (String pkg : appPowerUsageMap.keySet()) {
//                JSONArray appPowerUsageArray = new JSONArray();
//                JSONObject appPowerUsage = new JSONObject();
//
//                for (PowerUsageRecord element : appPowerUsageMap.get(pkg)) {
//                    JSONObject usageInfo = new JSONObject();
//                    usageInfo.put(JSON_KEY_USAGER, element.usage);
//                    usageInfo.put(JSON_KEY_FOREGROUND_USAGE, element.fgUsage);
//                    usageInfo.put(JSON_KEY_TIME, element.time);
//                    usageInfo.put(JSON_KEY_BOOT_GROUP, element.bootGroup);
//
//                    appPowerUsageArray.put(usageInfo);
//                }
//
//                appPowerUsage.put(JSON_KEY_PKG, pkg);
//                appPowerUsage.put(JSON_KEY_POWER_USAGER_INFOS, appPowerUsageArray);
//
//                jsonArray.put(appPowerUsage);
//            }
//
//            writeFile(jsonArray.toString());
//
//            if (DEBUG) {
//                Log.d(TAG, "持久化各app电池用量成功！");
//            }
//        } catch (JSONException e) {
//            if (DEBUG) {
//                Log.d(TAG, "持久化各app电池用量异常，原因：\n" + e.getLocalizedMessage());
//            }
//        } catch (IOException e) {
//            if (DEBUG) {
//                Log.d(TAG, "持久化各app电池用量异常，原因：\n" + e.getLocalizedMessage());
//            }
//        } finally {
//            writeUnLock("savePowerUsage()");
//        }
//    }
//
//    private String readFile(File file) throws IOException {
//        String content = "";
//
//        FileReader fileReader = null;
//        BufferedReader bufferedReader = null;
//
//        try {
//            fileReader = new FileReader(file);
//            bufferedReader = new BufferedReader(fileReader);
//
//            content = bufferedReader.readLine();
//        } finally {
//            try {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//
//                if (fileReader != null) {
//                    fileReader.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return content;
//    }
//
//    private void writeFile(String content) throws IOException {
//        File file = new File(getFilePath() + APP_POWER_USAGE_FILE_NAME);
//        if (file.exists()) {
//            file.delete();
//        }
//
//        FileWriter fwriter = null;
//        BufferedWriter bufferedWriter = null;
//        try {
//            fwriter = new FileWriter(file);
//            bufferedWriter = new BufferedWriter(fwriter);
//            bufferedWriter.write(content);
//        } finally {
//            try {
//                bufferedWriter.close();
//
//                fwriter.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private String getFilePath() {
//        File file;
//
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            file = new File(
//                    Environment.getExternalStorageDirectory().getAbsoluteFile() + "/360/batteryPlus/appPowerUsage");
//        } else {
//            file = new File(BatteryPlusApplication.getAppContext().getFilesDir().getAbsolutePath() + "/appPowerUsage");
//        }
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        return file.getAbsolutePath() + "/";
//    }
//
//    private void readLock(String tag) {
//        readLock.lock();
//
//        if (DEBUG) {
//            Log.e(TAG, tag + " readLock");
//        }
//    }
//
//    private void readUnLock(String tag) {
//        readLock.unlock();
//
//        if (DEBUG) {
//            Log.e(TAG, tag + " readUnLock");
//        }
//    }
//
//    private void writeLock(String tag) {
//        writeLock.lock();
//
//        if (DEBUG) {
//            Log.e(TAG, tag + " writeLock");
//        }
//    }
//
//    private void writeUnLock(String tag) {
//        if (!writeLock.isHeldByCurrentThread()) {
//            return;
//        }
//        writeLock.unlock();
//
//        if (DEBUG) {
//            Log.e(TAG, tag + " writeUnLock");
//        }
//    }
}

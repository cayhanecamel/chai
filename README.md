Chai
====
Android向けの開発時に役立つライブラリ郡です。  
開発時に確認したいアプリ内部のデータを手軽に確認することができます。  
ChaiはSQLiteを内部で利用していますが、プロダクト側と別のDBを利用しています。  
  

機能一覧
------
- デバッグ用メニュー画面   
- アプリ、端末情報の確認  
- SQLite viewer  
- App history
- Shared preference viewer



使い方
---

### Chaiセットアップ
```
DebugUtil.setup(this.getApplicationContext(), MyOpenHelper.DB_NAME, 
                MyOpenHelper.DB_VERSION, MyApplication.IS_DEBUGGABLE);
```

### デバッグメニューの呼び出し方法
```
Intent debug = new Intent(getApplicationContext(), DebugMenuActivity.class);
debug.putExtra(ProductInfoKeys.DB_NAME, MyOpenHelper.DB_NAME);
debug.putExtra(ProductInfoKeys.DB_VERSION, MyOpenHelper.DB_VERSION);
debug.putExtra(ProductInfoKeys.IS_DEBUG, Const.isDebug());
startActivity(debug)
``` 
    
App historyについて
------
Chai経由で出力されたログは全てApp history画面で見ることができます。   
出力方法は2種類で、両クラスとも出力されたログはDBに保存され後から閲覧可能です。  

ChaiLog
------   
Logcatと同じ優先度でApp historyへログを出力するクラスです。  
Logcat自体とは無関係です。  

AppHistoryUtil  
------ 
サーバAPIを利用する際に見やすく整形したログを出力するクラスです。  
サーバとのやりとりは基本Jsonを前提としています。
### AppHistoryUtilの使い方 

```
// リクエスト
AppInfo appInfo = new AppInfo();
appInfo.server = Const.API_HOST;
appInfo.type = Type.WEB_API_REQUEST;
appInfo.json = “送信内容”;
appInfo.header = “ヘッダー情報”;
mRequestSeq = AppHistoryUtil.add(appInfo);

// レスポンス
AppInfo info = new AppInfo();
info.server = Const.API_HOST;
info.type = Type.WEB_API_RESPONSE;
info.json = “受信内容”;
info.seq = mRequestSeq;
AppHistoryUtil.add(info);
```

    
### App historyの呼び出し方法
```
Intent debug = new Intent(getApplicationContext(), AppHistoryActivity.class);
startActivity(debug)
```
    
### テーブルビューでUNIXTIME→DateFormatへの変換設定
```
TableInfo tableInfo2 = new TableInfo();
tableInfo2.tableName = "CONTENTS";
tableInfo2.columName = "SALE_START_DATE";
tableInfo2.convert = TableInfo.ConvertType.DATE;
tableInfo2.multiply = 1000; //状況に応じて
DebugUtil.addTableInfo(tableInfo2);
```



### Shared preference viewerの使い方 ###
設定は特に必要がありません。
機能一覧：
- Shared preferenceファイル一覧表示
- Shared preferenceファイル削除
- Shared preferenceファイル編集


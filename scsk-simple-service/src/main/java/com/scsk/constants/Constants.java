package com.scsk.constants;

/**
 * 定数クラス
 */
public final class Constants {

	// データベース名前
	public static final String DB_NAME = "insight";
	// データベースのView名
	public static final String DB_VIEW = "insightView";
	// メール拡張名
	public static final String EMAILEND = "@scsk.jp";
	// admin権限
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	// user権限
	public static final String ROLE_USER = "ROLE_USER";
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMddHHmmss";
	// 日付フォーマット
	public static final String DATE_SENDEMAIL = "yyyy年MM月dd日HH時mm分";
	// 日付フォーマット
	public static final String DATE_SENDEMAIL_1 = "yyyy年MM月dd日 HH:mm";
	// 日付フォーマット
	public static final String DATE_FORMAT_ASS = "yyyyMMddHHmmssSSS";
	// 日付フォーマット
	public static final String DATE_FORMAT_APP = "yyyyMM";
	// 日付フォーマット
	public static final String DATE_FORMAT_APP_DATE = "yyyyMMdd";
	// 登録・更新日時フォーマット
	public static final String DATE_FORMAT_DB = "yyyy/MM/dd HH:mm:ss";
	// 日時フォーマット
	public static final String DATE_FORMAT_YMD = "yyyy/MM/dd";
	// 日付フォーマット
	public static final String DATE_FORMAT_APP_DATE2 = "yyyy-MM-dd";
	// PDF拡張名
	public static final String PDF = ".pdf";
	// CSV拡張名
	public static final String CSV = ".csv";
	// 圧縮ファイルの拡張名
	public static final String ZIP = ".zip";
	// TXT
	public static final String TXT=".txt";

	public static final String SESSIONUSERINFO = "sessionUserInfo";
	public static final String LONGINID = "loginID";
	public static final String USERID = "userID";
	public static final String HIDUSERID = "hidUserID";
	public static final String PASSWORD = "password";
	public static final String COUNT = "count";
	public static final String SELECTFLG = "selectFlg";
	public static final String MESSAGEID = "errMessageID";
	public static final String FLG = "flg";
	public static final String LOCKSTATUS = "lockStatus";
	public static final String LONGINNAME = "loginNAME";

	// 戻るステータスコード
	public static final String RESULT_STATUS_OK = "OK";
	public static final String RESULT_STATUS_NG = "NG";
	public static final String SYSTEMERROR_CODE = "500";
	public static final String RESULT_SUCCESS_CODE = "I0000001";

	// ステータス
	public static final String APPLY_STATUS_1 = "受付";
	public static final String APPLY_STATUS_2 = "処理中";
	public static final String APPLY_STATUS_3 = "完了";

	// LogInfo
	public static final String IMPLEMENT_START = "実行開始";
	public static final String IMPLEMENT_END = "実行完了";
	public static final String SENNI = "画面へ遷移";

	public static final String INDEX = "条件検索";
	public static final String FIND = "単一検索処理";
	public static final String VIEW = "無条件検索";
	public static final String DYNAMICVIEW = "ビュー検索";
	public static final String SAVE = "登録処理";
	public static final String UPDATE = "更新処理";
	public static final String DELETE = "(論理)削除処理";
	public static final String REMOVE = "(物理)削除処理";
	public static final String LOGMESSAGE001 = "ビュー({})削除失敗";
	public static final String LOGMESSAGE002 = "ビュー({})削除異常";

	// ステータスMessage
	public static final String ACCOUNT_SEQ = "\n受付番号：\n";
	public static final String RECEIPT_DATE = "受付日時：\n";
	public static final String APPLY_KIND = "\n申込種別：\nひろぎんネット支店口座開設\n\n";

	public static final String PUSH_MESSAGE_STATUS_1 = "[ひろぎんネット支店より]"
	        + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございます。"
			+ "\n\nお客さまの申込を受付しました。"
			+ "\n口座開設完了まで今しばらくお待ちください。"
			+ "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_2 = "[ひろぎんネット支店より]"
	        + "\nお客さまの口座開設手続きを開始いたしました。"
	        + "\n\n近日中（平日の9：00～17：00）にお届けいただいた電話番号にご本人さま確認のお電話をさせていただきますのでよろしくお願いいたします。"
	        + "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_3 = "[ひろぎんネット支店より]"
	        + "\nお客さまの口座開設手続きが完了しました。"
	        + "\n\n近日中にキャッシュカードおよびダイレクトバンキングサービスご利用カードをそれぞれ「本人限定受取郵便（特定事項伝達型）」にて送付いたします。"
			+ "\nお受け取り時には本人確認書類をご提示ください。"
	        + "\n\n郵便物をお受け取りいただけなかった場合、お申込みを無効とさせていただくことがございます。"
			+ "\n\nまた、キャッシュカードとともにお送りする印鑑票はご記入、ご捺印のうえ同封の返信用封筒にて速やかにご返送いただきますようお願いいたします。"
	        + "\n\n引き続きひろぎんネット支店をご愛顧いただきますようお願い申し上げます。"
	        + "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_4 = "[ひろぎんネット支店より]"
            + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございました。"
            + "\n\n誠に申し訳ございませんが、今回のお申込みはお受付できませんでした。"
            + "\n詳細については、お申込みいただいた住所宛に郵送でお送りさせていただきますので、そちらでご確認ください。"
            + "\n\nなお、お申込データは、こちらで破棄させていただきますので、何卒ご了承ください。"
            + "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_5 = "[ひろぎんネット支店より]"
	        + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございました。"
			+ "\n\n誠に申し訳ございませんが、今回のお申込みはお受付できませんでした。"
			+ "\n詳細については、お申込みいただいた住所宛に郵送でお送りさせていただきますので、そちらでご確認ください。"
			+ "\n\nなお、お申込データは、こちらで破棄させていただきますので、何卒ご了承ください。"
			+ "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_6 = "[ひろぎんネット支店より]"
	        + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございました。"
			+ "\n\n誠に申し訳ございませんが、今回のお申込みはお受付できませんでした。"
			+ "\n詳細については、お申込みいただいた住所宛に郵送でお送りさせていただきますので、そちらでご確認ください。"
			+ "\n\nなお、お申込データは、こちらで破棄させていただきますので、何卒ご了承ください。"
			+ "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_7 = "[ひろぎんネット支店より]"
	        + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございました。"
			+ "\n\n誠に申し訳ございませんが、今回のお申込みはお受付できませんでした。"
			+ "\n詳細については、お申込みいただいた住所宛に郵送でお送りさせていただきますので、そちらでご確認ください。"
			+ "\n\nなお、お申込データは、こちらで破棄させていただきますので、何卒ご了承ください。"
			+ "\n\n";
	
	public static final String PUSH_MESSAGE_STATUS_8 = "[ひろぎんネット支店より]"
            + "\nこのたびは口座開設のお申込みをいただきまして、誠にありがとうございました。"
            + "\n\n今回のお申込みはご本人さまのお申出により、取り下げとさせていただきました。"
            + "\n\nなお、お申込データは、こちらで破棄させていただきますので、何卒ご了承ください。"
            + "\n\n";
	
	public static final String PUSH_MESSAGE_ABOUT = "【お問い合わせ】\nひろぎんネット支店\nTEL：0120-93-1645\n平日／9:00 ～ 17:00\n（土・日・祝休日、および大晦日・正月3が日は除く）\n";

	// PUSH通知正常状態コード
	public static final String RETURN_OK = "202";

	// ドキュメントタイプ
	public static final String DOCTYPE_1 = "PUSHRECORD";
	public static final String DOCTYPE_2 = "ACCOUNTSUM";
	public static final String DOCTYPE_3 = "AUTHORITY";
	public static final String DOCTYPE_4 = "ACTIONLOG";
	public static final String DOCTYPE_5 ="STOREATMDATAFILE";

	// 帳票/CSV表示用文字
	public static final String OCCUPATION_1 = "会社役員/ 団体役員";
	public static final String OCCUPATION_2 = "会社員/ 団体職員";
	public static final String OCCUPATION_3 = "公務員";
	public static final String OCCUPATION_4 = "個人事業主/ 自営業";
	public static final String OCCUPATION_5 = "パート／アルバイト／派遣社員／契約社員";
	public static final String OCCUPATION_6 = "主婦";
	public static final String OCCUPATION_7 = "学生";
	public static final String OCCUPATION_8 = "退職された方／無職の方";

	public static final String SEX_1 = "男";
	public static final String SEX_2 = "女";
	public static final String DAY_1 = "月曜日";
	public static final String DAY_2 = "火曜日";
	public static final String DAY_3 = "水曜日";
	public static final String DAY_4 = "木曜日";
	public static final String DAY_5 = "金曜日";
	public static final String DAY_6 = "土曜日";
	public static final String DAY_7 = "日曜日";
	
	public static final String YEAR = "年";
	public static final String MONTH_JP = "月";
	public static final String DAY = "日";

	public static final String TRANSACTIONTYPE = "普　通";

	public static final String MARK1 = "〇";
	public static final String MARK2 = "~";
	public static final String MARK3 = "-";
	public static final String MARK4 = "/";

	public static final String SHOPNO_1 = "2";
	public static final String SHOPNO_2 = "9";
	public static final String SHOPNO_3 = "1";
	public static final String SHOPNAME = "ひろぎんネット支店";

	public static final String IDENTIFICATIONTYPE_1 = "運転免許証";
	public static final String IDENTIFICATIONTYPE_2 = "パスポート";
	public static final String IDENTIFICATIONTYPE_3 = "住基カード";
	public static final String IDENTIFICATIONTYPE_4 = "マイナンバーカード";
	public static final String LIVINGCONDITIONS_1 = "住民票";
	public static final String LIVINGCONDITIONS_2 = "公共料金領収証";

	public static final String STOREATM_DOCTYPE = "STOREATMINFO";
	public static final String USER_DOCTYPE = "USER";
	public static final String IMAGEFILE_DOCTYPE = "IMAGEFILE";
	public static final String USERACTIONLOG_DOCTYPE = "USERACTIONLOG";
	public static final String PWDHISTORY_DOCTYPE = "PWDHISTORY";
	public static final String COLON = ":";
	
	public static final String VERSIONDOC_DOCTYPE = "VERSIONDOC";
	
	public static final String HOLDACCOUNT_0 = "無";
	public static final String HOLDACCOUNT_1 = "有";
	
	public static final String TRADINGPURPOSES_1 = "生活費決済";
	public static final String TRADINGPURPOSES_2 = "給料受取/ 年金受取";
	public static final String TRADINGPURPOSES_3 = "貯蓄/ 資産運用";
	public static final String TRADINGPURPOSES_4 = "融資";
	public static final String TRADINGPURPOSES_5 = "外国為替取引";
	
	public static final String ACCOUNTAPPMOTIVE_1 = "近くに<ひろぎん>の店舗がないから";
	public static final String ACCOUNTAPPMOTIVE_2 = "日中店頭に行く時間がないから";
	public static final String ACCOUNTAPPMOTIVE_3 = "24時間申込み、取引が可能だから";
	public static final String ACCOUNTAPPMOTIVE_4 = "給与振込や公共料金の引落口座に使いたいから";
	public static final String ACCOUNTAPPMOTIVE_5 = "当行から案内されたから";
	public static final String ACCOUNTAPPMOTIVE_6 = "ネットオークションに利用したいから";
	public static final String ACCOUNTAPPMOTIVE_7 = "ペイオフ対策";
	public static final String ACCOUNTAPPMOTIVE_8 = "定期預金等の金利にを魅力感じたから";
	public static final String ACCOUNTAPPMOTIVE_9 = "広島銀行との取引を希望するから";
	public static final String ACCOUNTAPPMOTIVE_10 = "学生証・社員証の作成に必要だから";
	public static final String ACCOUNTAPPMOTIVE_90 = "その他";
	
	public static final String KNOWPROCESS_1 = "ひろぎんホームページ";
	public static final String KNOWPROCESS_2 = "ひろぎんホームページ以外のサイト";
	public static final String KNOWPROCESS_3 = "ウェフ広告";
	public static final String KNOWPROCESS_4 = "新聞広告";
	public static final String KNOWPROCESS_5 = "行員の案内";
	public static final String KNOWPROCESS_6 = "当行からのＤＭや電子メール";
	public static final String KNOWPROCESS_7 = "その他";
	
	// ＩＰアドレス/電話番号鑑定
	public static final String MONTH = "ヵ月";
	public static final String PROXYVALUE_1 = "要注意（利用している可能性あり）";
	public static final String PROXYVALUE_2 = "-";
	public static final String THREAT_1 = "high";
	public static final String THREAT_2 = "low";
	public static final String THREATVALUE_1 = "要注意";
	public static final String THREATVALUE_2 = "低い";
	public static final String PSIP_1 = "true";
	public static final String PSIP_2 = "false";
	public static final String PSIPVALUE_1 = "要注意";
	public static final String PSIPVALUE_2 = "-";
	public static final String ISMOBILEVALUE = "携帯電話ＩＰアドレス以外";
	public static final String RESULTVALUE_1 = "検索成功";
	public static final String RESULTVALUE_2 = "データ無し";
	public static final String RESULTVALUE_3 = "電話番号の桁数不正";
	public static final String RESULTVALUE_4 = "特殊電話番号等調査対象外、予期しないエラー";
	public static final String ATTENTIONVALUE_1 = "MSG該当なし：｢直近加入」「都合停止」「変更過多」｢長期有効」「エラー」いずれにも該当しない場合";
	public static final String ATTENTIONVALUE_2 = "直近加入：三カ月以内に有効になった番号";
	public static final String ATTENTIONVALUE_3 = "都合停止：半年以内に都合停止がある番号";
	public static final String ATTENTIONVALUE_4 = "変更過多：有効無効を3回以上繰り返している番号";
	public static final String ATTENTIONVALUE_5 = "長期有効：24ヵ月以上有効である番号";
	public static final String ATTENTIONVALUE_6 = "ｴﾗｰ：特殊番号等で調査不可";
	public static final String ATTENTIONVALUE_7 = "未調査：対象ﾃﾞｰﾀが無い番号";
	public static final String TACSFLAGVALUE_1 = "無効：電話番号が使われていない";
	public static final String TACSFLAGVALUE_2 = "有効：電話番号が使われている";;
	public static final String TACSFLAGVALUE_3 = "移転：移転ｱﾅｳﾝｽが流れている電話番号";
	public static final String TACSFLAGVALUE_4 = "都合停止：お客様都合で利用ができない電話番号";
	public static final String TACSFLAGVALUE_5 = "ｴﾗｰ：判定できない電話番号もしくは050、0120局番等の電話番号";
	public static final String TACSFLAGVALUE_6 = "局預け：電話局に電話番号を預けている";
	public static final String TACSFLAGVALUE_7 = "再調査：判定できない電話番号";
	public static final String TACSFLAGVALUE_8 = "INS回線有効：電話番号が使われている（ISDN回線）";
	public static final String TACSFLAGVALUE_9 = "NTT以外：NTT以外で一部判定ができない電話番号";
	public static final String ACTIONLOG_LOGIN_1 = "【ログイン画面】ユーザーIDかパスワードが間違っています";
	public static final String ACTIONLOG_LOGIN_2 = "【ログイン画面】ユーザーIDがロックされています";
	public static final String ACTIONLOG_LOGIN_3 = "【ログイン画面】ユーザー登録";
	public static final String ACTIONLOG_PASSWORD_1 = "【パスワード変更画面】初期化パスワードですが、パスワード変更が必要";
	public static final String ACTIONLOG_PASSWORD_2 = "【パスワード変更画面】パスワードを連続５回以上間違えた、アカウントがロックアウト";
	public static final String ACTIONLOG_PASSWORD_3 = "【パスワード変更画面】最終パスワード変更日時により、90日以上ですが、パスワード変更が必要";
	public static final String ACTIONLOG_PASSWORD_4 = "【パスワードリセット画面】ログイン画面でパスワード忘れボタン押下";
	public static final String ACTIONLOG_PASSWORD_5 = "【パスワードリセット画面】パスワードリセット";
	public static final String ACTIONLOG_PASSWORD_6 = "【パスワード変更画面】パスワード変更";
	public static final String ACTIONLOG_PASSWORD_7 = "【パスワード変更画面】本日は既にパスワードを変更済のため、再度変更できない。";
	public static final String ACTIONLOG_PASSWORD_8 = "【パスワード変更画面】入力されたパスワードは直近利用されています（過去５世代）";
	public static final String ACTIONLOG_AUTHORITY_1 = "【権限一覧画面】権限一覧表示";
	public static final String ACTIONLOG_AUTHORITY_2 = "【権限一覧画面】権限一括削除";
	public static final String ACTIONLOG_AUTHORITY_3 = "【権限新規登録画面】権限一覧画面で新規登録ボタン押下";
	public static final String ACTIONLOG_AUTHORITY_4 = "【権限新規登録画面】権限新規登録";
	public static final String ACTIONLOG_AUTHORITY_5 = "【権限詳細／編集画面】権限一覧画面で詳細／編集ボタン押下";
	public static final String ACTIONLOG_AUTHORITY_6 = "【権限詳細／編集画面】権限情報更新";
	public static final String ACTIONLOG_STOREATM_1 = "【店舗ATM一覧画面】店舗ATM一覧表示";
	public static final String ACTIONLOG_STOREATM_2 = "【店舗ATM一覧画面】店舗ATM一括削除";
	public static final String ACTIONLOG_STOREATM_3 = "【店舗ATM新規登録画面】店舗ATM一覧画面で新規登録ボタン押下";
	public static final String ACTIONLOG_STOREATM_4 = "【店舗ATM新規登録画面】店舗ATM新規登録";
	public static final String ACTIONLOG_STOREATM_5 = "【店舗ATM詳細／編集画面】店舗ATM一覧画面で詳細／編集ボタン押下";
    public static final String ACTIONLOG_STOREATM_6 = "【店舗ATM詳細／編集画面】店舗ATM情報更新";
    public static final String ACTIONLOG_ACCOUNT_1 = "【申込一覧画面】申込一覧表示";
    public static final String ACTIONLOG_ACCOUNT_2 = "【申込一覧画面】一覧出力";
    public static final String ACTIONLOG_ACCOUNT_3 = "【申込一覧画面】顧客CSV出力";
    public static final String ACTIONLOG_ACCOUNT_4 = "【申込一覧画面】完了消込";
    public static final String ACTIONLOG_ACCOUNT_5 = "【申込詳細／編集画面】申込一覧画面で詳細／編集ボタン押下";
    public static final String ACTIONLOG_ACCOUNT_6 = "【申込詳細／編集画面】ステータス変更";
    public static final String ACTIONLOG_PUSHPRECORD_1 = "【PUSH通知承認】PUSHメッセージ一覧表示";
    public static final String ACTIONLOG_PUSHPRECORD_2 = "【PUSH通知承認】承認";
    public static final String ACTIONLOG_PUSHPRECORD_3 = "【PUSH通知承認】一括承認";
    public static final String ACTIONLOG_PUSHPRECORD_4 = "【PUSH通知承認_詳細画面】PUSH通知承認一覧画面で詳細ボタン押下";
    public static final String ACTIONLOG_LOGOUT_1 ="【共通】ログアウト";
    public static final String ACTIONLOG_LOGOUT_2 ="【共通】セッション切れ、強制ログアウト";
    public static final String ACTIONLOG_USERLIST_1="【ユーザー一覧画面】ユーザー一覧表示";
    public static final String ACTIONLOG_USERLIST_2="【ユーザー一覧画面】ユーザー一括削除";
    public static final String ACTIONLOG_USERLIST_3="【ユーザー一覧画面】ユーザー一覧CSV出力";
    public static final String ACTIONLOG_USERLIST_4="【ユーザー一覧画面】ユーザー操作／行動ログCSV出力";
    public static final String ACTIONLOG_USERLIST_5="【ユーザー新規登録画面】ユーザー一覧画面で新規登録ボタン押下";
    public static final String ACTIONLOG_USERLIST_6="【ユーザー新規登録画面】ユーザー新規登録";
    public static final String ACTIONLOG_USERLIST_7="【ユーザー詳細／編集画面】ユーザー一覧画面で詳細／編集ボタン押下";
    public static final String ACTIONLOG_USERLIST_8="【ユーザー詳細／編集画面】ユーザー情報更新";
    public static final String ACTIONLOG_USERLIST_9="【ユーザー詳細／編集画面】ユーザーのパスワードリセット";
    public static final String ACTIONLOG_HTML_PDF_FILE_1="【更新用HTMLとPDFファイル管理画面】HTMLとPDFファイル管理画面表示";
    public static final String ACTIONLOG_HTML_PDF_FILE_2="【更新用HTMLとPDFファイル管理画面】更新履歴検索";
    public static final String ACTIONLOG_HTML_PDF_FILE_3="【更新用HTMLとPDFファイル管理画面】ファイルアップロード";
    public static final String ACTIONLOG_HTML_PDF_FILE_4="【更新用HTMLとPDFファイル管理画面】ファイルダウンロード";
    public static final String ACTIONLOG_HTML_PDF_FILE_5="【更新用HTMLとPDFファイル管理画面】一括削除";
    public static final String ACTIONLOG_STORE_ATM_DATA_1="【更新用店舗ATMマスタデータ管理画面】店舗ATMマスタデータファイル管理画面表示";
    public static final String ACTIONLOG_STORE_ATM_DATA_2="【更新用店舗ATMマスタデータ管理画面】更新履歴検索";
    public static final String ACTIONLOG_STORE_ATM_DATA_3="【更新用店舗ATMマスタデータ管理画面】ファイルアップロード";
    public static final String ACTIONLOG_STORE_ATM_DATA_4="【更新用店舗ATMマスタデータ管理画面】ファイルダウンロード";
    public static final String ACTIONLOG_STORE_ATM_DATA_5="【更新用店舗ATMマスタデータ管理画面】一括削除";
    public static final String ACTIONLOG_IMAGE_FILE_1="【画面表示用画像＆URL管理画面】画像＆URL管理画面表示";
    public static final String ACTIONLOG_IMAGE_FILE_2="【画面表示用画像＆URL管理画面】検索処理";
    public static final String ACTIONLOG_IMAGE_FILE_3="【画面表示用画像＆URL管理画面】画像ファイル一括削除";
    public static final String ACTIONLOG_IMAGE_FILE_4="【画像＆URL新規登録画面】画像＆URL管理画面画面で新規登録ボタン押下";
    public static final String ACTIONLOG_IMAGE_FILE_5="【画像＆URL新規登録画面】画像ファイル新規登録";
    public static final String ACTIONLOG_IMAGE_FILE_6="【画像＆URL詳細／編集画面】画像＆URL管理画面で詳細／編集ボタン押下";
    public static final String ACTIONLOG_IMAGE_FILE_7="【画像＆URL詳細／編集画面】画像ファイル情報更新";
    
    
    
    public static final String GETLIST = "function(doc) { if(doc.docType && doc.docType === \"%s\" && doc.delFlg && doc.delFlg === \"0\"){emit(doc._id, 1);} }";
}


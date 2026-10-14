# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 專案性質

這是一份 Java 面試考題的**模板專案**（線上餐點點餐系統 + 訂單號碼配置），完整題目與規則都在 `README.md`。應試者會下載此 repo 的 ZIP 並在本機作答。

- 現有類別中的 `// TODO` stub 是刻意留給應試者的空白。除非使用者明確要求實作解答，否則不要填寫答案；修改題目時應同步更新 `README.md`。
- README 規定：**不可修改既有檔案名稱與目錄結構**，可自由新增類別。
- `Main.java` 中的統計輸出字串為題目預期的輸出格式，調整時需與 README 一致。

## 指令

使用 JDK 17 + Maven（repo 沒有 Maven Wrapper，需本機安裝 `mvn`）。

```bash
mvn compile                                   # 編譯
mvn test                                      # 執行全部測試（JUnit 5）
mvn test -Dtest=ClassName                     # 執行單一測試類別
mvn test -Dtest=ClassName#methodName          # 執行單一測試方法
java -cp target/classes com.mealorder.Main    # 編譯後執行主程式
```

`pom.xml` 未設定 `exec-maven-plugin`，也尚未有 `src/test` 目錄；新增 JUnit 測試時放在 `src/test/java/com/mealorder/...`。

## 架構（`com.mealorder`）

- `Main`：主程式。以 `OrderNumberFactory.generateOrderNumber(2000)` 產生號碼池，再啟動 2 個執行緒，各自建立 500 份 `FeastMeal` 與 500 份 `LightMeal`，並從共用號碼池指派號碼，最後輸出統計。
- `factory.OrderNumberFactory`：**interface 上的 static method**，回傳的單一 `List<String>` 同時混合輕食餐（5 碼）與豐盛餐（7 碼）號碼；餐點必須依自身規則從中挑出未使用、符合規則的號碼。
- `model.LightMeal` / `model.FeastMeal`：題目要求的兩個獨立類別，價格與卡路里以亂數產生，提供 `printInfo()`。
- `service/`：空目錄（未被 git 追蹤），預留給應試者放置服務邏輯。

## 實作時容易忽略的規則（細節見 README）

- 號碼產生流程順序：產生 → 重複時將英文/數字兩段對調再試 → 仍重複則棄用 → **全部產生完後**才移除含 `KK` 或 `77` 的號碼。因此最終數量會少於 `quantity`。
- 輕食餐：英文可重複、數字不可重複；豐盛餐：英文不可重複、數字可重複但不可含 `4`。
- 兩個執行緒共用同一號碼池，指派必須 thread-safe，且同一號碼不可被配置兩次。
- 某餐點類型一旦無法配置號碼，該執行緒中該類型剩餘的餐點全部捨棄（不影響另一類型）。

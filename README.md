# Git-for-Datapack
GitHubのリポジトリからデータパックを更新するSpigotプラグイン

たぶんセキュリティホールだらけなので修正しなきゃ

Publicにするの初めてなのでいろいろ改善点があるはず

## 動作環境
以下の環境でビルド・動作が確認できている(おそらく**1.21.11のSpigotが実行できる環境**なら実行可能)
- Windows 11
- Minecraft Java Edition 1.21.11
- Spigot 1.21.11
- Java 25

## 下準備
- GitHubにて[Fine-grained Personal Access Token](https://docs.github.com/ja/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens)を生成
  - "Repository access"にデータパック開発に使用しているリポジトリを追加
  - "Permissions"は"Contents"を"Read-only"に指定
- サーバー側の環境変数に生成したTokenを設定(初期設定では環境変数`G4D_GITHUBTOKEN`を参照する)
- config.ymlを必要に応じて編集(初回起動時`./plugins/Git-for-Datapack/`に生成される)

## 使用方法
- Minecraft内で以下のコマンドを実行するとリポジトリを登録できる
  - ```/g4d-connect データパック名 リポジトリのURL(HTTPS) ブランチ名 リポジトリ内のパス```
- その状態で以下のコマンドを実行するとリモートから再読み込みされる
  - ```/g4d-reload```

詳細な使用法は**コマンド一覧**を参照

## コマンド一覧
| コマンド | 説明 | 使用例 |
| :------- | :--- | :----- |
| `g4d-connect データパック名 リポジトリのURL (ブランチ名 リポジトリ内のパス)` | データパック名とリモートリポジトリを登録する[^connect] | `/g4d-connect example_pack "https://github.com/example/example-repository.git" main "./src/"` |
| `g4d-disconnect データパック名` | リポジトリの登録情報を削除する[^disconnect] | `/g4d-disconnect example_pack` |
| `g4d-info データパック名` | リポジトリの登録情報を表示する | `/g4d-info example_pack` |
| `g4d-list` | 登録されたデータパック名の一覧を表示 | `/g4d-list` |
| `g4d-reload (データパック名...)` | 登録されたリポジトリからデータパックを再読み込みする[^reload] | `/g4d-reload example_pack example_pack2` |
| `g4d-update データパック名 項目 値` | リポジトリの登録情報を編集する[^update] | `/g4d-update URI "https://github.com/example/example-datapack.git"` |

[^connect]: ブランチ, パスは省略可能
  省略時は"main", "./"が使用される

[^disconnect]: ワールドフォルダー内のデータパックファイルは削除されない

[^reload]: データパック名は省略可能
  省略時は登録されたすべてのリポジトリから再読み込みされる

[^update]: 以下の項目が編集できる
     - URI: リポジトリのURL
     - branch: ブランチ名
     - directory: リポジトリ内のデータパックファイルまでのパス

## config.yml
プラグイン導入後の初回起動時`./plugins/Git-for-Datapack/`に生成される

| 設定項目 | 初期値 | 説明 |
| :------- | :----- | :--- |
| datapacks-path | ./world/datapacks/ | ワールドデータ内のdatapacksフォルダのパスを指定 |
| workspace-path | ./G4D_workspace/ | ローカルリポジトリを格納するフォルダのパスを指定 |
| token-env | G4D_GITHUBTOKEN | GitHubのリポジトリにアクセスするためのTokenを指定 |
| mc-reloadcommand | minecraft:reload | Minecraftのデータパック再読み込みコマンドを指定 |
| default | | g4d-connectで入力するbranch, directoryの初期値を指定[^connect] |
| datapacks | | コマンドで設定したリポジトリが反映される |

## 外部ライブラリ
> ### [**JGit**](https://projects.eclipse.org/projects/technology.jgit) 7.5.0:
> - ライセンス: "Eclipse Distribution License - v 1.0"
> - https://www.eclipse.org/org/documents/edl-v10/

ライブラリの著作権は作成者に帰属します。

## ライセンス

**Copyright (C) 2025 Darjeeling\<T>**

This program is dual licensed under the `GPL-3.0-or-later` OR `LGPL-3.0-or-later`.

> ### **GNU General Public License Version 3.0**
> - https://www.gnu.org/licenses/gpl-3.0.en.html

> ### **GNU Lesser General Public License Version 3.0**
> - https://www.gnu.org/licenses/lgpl-3.0.en.html
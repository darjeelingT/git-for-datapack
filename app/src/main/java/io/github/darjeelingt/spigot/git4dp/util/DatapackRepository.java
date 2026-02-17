package io.github.darjeelingt.spigot.git4dp.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
public record DatapackRepository(String datapackname, URI remoteURI, String branch, Path directory) {

    public static DatapackRepository toDPRepository(Map<?, ?> packmap)
        throws IllegalArgumentException
    {
        String uristring = packmap.get("URI").toString();
        try {
            String name = packmap.get("name").toString();
            if (name.indexOf(File.pathSeparator) >= 0) {// nameにパス区切り文字(/ or \)が入っている場合IllegalArgumentExceptionをスロー
                throw new IllegalArgumentException(String.format("Invalid Name: \"%s\"", uristring));
            }


            URI uri = new URI(uristring);// URIへの変換に失敗した場合はURISyntaxExceptionがスローされる

            if (!uri.getPath().endsWith(".git")) {// リポジトリのパス部分が.gitで終わらない場合IllegalArgumentExceptionをスロー
                throw new IllegalArgumentException(String.format("Invalid URL: \"%s\"", uristring));
            }

            String branchName = packmap.get("branch").toString();


            String pathString = packmap.get("directory").toString();
            Path path = Paths.get(pathString).normalize();
            if (path.startsWith("..") || path.isAbsolute()) {// ディレクトリがワークスペース内ではない(親ディレクトリを指定 or 絶対パスを指定)場合IllegalArgumentExceptionをスロー
                throw new IllegalArgumentException(String.format("Invalid Directory: \"%s\"", pathString));
            }

            return new DatapackRepository(
                name, 
                uri, 
                branchName, 
                path
            );
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Invalid URI: \"%s\"", uristring));
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("name", this.datapackname());
        result.put("URI", this.remoteURI().toString());
        result.put("branch", this.branch());
        result.put("directory", this.directory().toString());
        return result;
    }

    public String getRepositoryName() {
        String repositoryfilename = 
            Paths.get(
                this.remoteURI.getPath()// URIのパス部分を取得
            )
            .getFileName()// Pathからファイル名を取得
            .toString();
        return repositoryfilename.substring(
            0, 
            repositoryfilename.lastIndexOf(".git")
        );// ファイル名の".git"部分を削除(インスタンス生成時に拡張子が".git"であることは確認済み)

    }
};

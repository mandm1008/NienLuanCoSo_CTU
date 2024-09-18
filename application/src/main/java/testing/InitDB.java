package testing;

import db.SongModel;

public class InitDB {

    private SongModel songModel;

    public InitDB(String title, String href, String image) {
        songModel = new SongModel(title, href, image);
    }

    public void save() {
        songModel.insert();
    }

    public static void main(String[] args) {
        new InitDB(
                "Áng mây vô tình",
                "https://drive.google.com/uc?export=download&id=11lSpMvavILLYt1tZVzIME_qXMGLzSu6J",
                "https://drive.google.com/uc?export=download&id=1c8d4_FHjHKi4Uq6cOmH3AiKUckzw7Qsg")
                .save();

        new InitDB(
                "Cho Mình Em",
                "https://drive.google.com/uc?export=download&id=1fvPeoGrW8YJGPMQOTN6X8Nl7GNr_ttx0",
                "https://drive.google.com/uc?export=download&id=10HfybCtERx8H4Xb-NkmkTYm1tXYcJRWx")
                .save();

        new InitDB(
                "Trốn tìm",
                "https://drive.google.com/uc?export=download&id=1eWvul47FgBXsBxLRbLydZS0lnaG3rDaq",
                "https://drive.google.com/uc?export=download&id=1TFgSXux8yp5_jWPqjJS0y4vuMKm9pYj_")
                .save();

        new InitDB(
                "Đường tôi chở em về",
                "https://drive.google.com/uc?export=download&id=1e_1nzy-Ywgf1y3OEFxaAA8VqmMtbDSmP",
                "https://drive.google.com/uc?export=download&id=1WXA0HVTOEBlemJwbc0L-WqO6ffygoCtd")
                .save();

        new InitDB(
                "Cho tôi lang thang",
                "https://drive.google.com/uc?export=download&id=1FxVjub9ZEiaVc3pNZTcPhhMmPKj7ortp",
                "https://drive.google.com/uc?export=download&id=1aHE0RECh44JCi_0pwMJI52NQyRrfi1LL")
                .save();

        new InitDB(
                "Người lạ ơi",
                "https://drive.google.com/uc?export=download&id=1kFSy4UQyXUGkL9xeU8cQ1b2_7dZb0231",
                "https://drive.google.com/uc?export=download&id=1YNAkDJO9lVpLQuzfi66IjD5lDzfMh-km")
                .save();

        new InitDB(
                "Người thứ ba",
                "https://drive.google.com/uc?export=download&id=1NzvBCmbXsmYIWz468jNYVwOFIDPM5QNC",
                "https://drive.google.com/uc?export=download&id=17MyHxv67p4HBDAq0L5E5Vr4wpKMoKmc0")
                .save();

        new InitDB(
                "Nhạt",
                "https://drive.google.com/uc?export=download&id=1oyP3m0RVzn_H5bwRClLPPeopgosRc7mo",
                "https://drive.google.com/uc?export=download&id=11QhvEYVa8vkPoXWmcTP9TVk7k5VsqQQA")
                .save();
    }

}

import fs, { existsSync, mkdirSync } from "fs";
import multer from "multer";
import { join } from "path";

const repDir: string = join(__dirname, "/../reports");
if (!existsSync(repDir)) {
    mkdirSync(repDir);
}

export const deleteReports = fs.stat(repDir, function (err, stats) {
    if (!err) deleteFilesRecursive(repDir);
});

export const deleteFilesRecursive = function (path: string, rmSelf?: string) {
    fs.readdir(path, (err, files) => {
        if (err) throw err;
        for (const file of files) {
            fs.unlink(join(path, file), (err) => {
                if (err) throw err;
            });
        }
    });
};

import { hash } from "bcrypt";
import { UserDto } from "@dtos/users.dto";
import { HttpException } from "@exceptions/HttpException";
import { IUser } from "@interfaces/users.interface";
import userModel from "@models/users.model";
import { isEmpty } from "@utils/util";
import bcrypt from "bcrypt";
import nodemailer from "nodemailer";
import CONFIG from "@config";
import { logger } from "@utils/logger";
import Handlebars from "handlebars";
import fs from "fs";

class UserService {
    public users = userModel;

    public async findAllUser(): Promise<IUser[]> {
        const users: IUser[] = await this.users.find();
        return users;
    }

    public async findUserById(userId: string): Promise<IUser> {
        if (isEmpty(userId)) throw new HttpException(400, "You're not userId");

        const findUser: IUser = await this.users.findOne({ _id: userId });
        if (!findUser) throw new HttpException(409, "You're not user");

        return findUser;
    }

    public async createUser(userData: UserDto): Promise<IUser> {
        if (isEmpty(userData)) throw new HttpException(400, "Bad request");

        const findUser: IUser = await this.users.findOne({
            email: userData.email,
        });
        if (findUser)
            throw new HttpException(
                409,
                `You're email ${userData.email} already exists`
            );

        const hashedPassword = await hash(userData.password, 10);
        const createUserData: IUser = await this.users.create({
            ...userData,
            password: hashedPassword,
        });

        return createUserData;
    }

    public async updateUser(userId: string, userData: UserDto): Promise<IUser> {
        if (isEmpty(userData)) throw new HttpException(400, "Bad request");

        if (userData.email) {
            const findUser: IUser = await this.users.findOne({
                email: userData.email,
            });
            if (findUser && findUser._id.toString() != userId)
                throw new HttpException(
                    409,
                    `You're email ${userData.email} already exists`
                );
        }

        if (userData.password) {
            const hashedPassword = await hash(userData.password, 10);
            userData = { ...userData, password: hashedPassword };
        }
        const updateUserById: IUser = await this.users.findByIdAndUpdate(
            userId,
            { ...userData },
            { new: true }
        );
        if (!updateUserById) throw new HttpException(409, "You're not user");

        return updateUserById;
    }

    public async deleteUser(userId: string): Promise<IUser> {
        const deleteUserById: IUser = await this.users.findByIdAndDelete(
            userId
        );
        if (!deleteUserById) throw new HttpException(409, "You're not user");

        return deleteUserById;
    }

    public async resetPassword(
        user: IUser,
        newPassword: string
    ): Promise<IUser> {
        if (isEmpty(user) || isEmpty(newPassword))
            throw new HttpException(400, "Bad request");
        const foundUser: IUser = await this.users.findById(user._id);
        if (isEmpty(foundUser))
            throw new HttpException(401, "You are not user");
        if (isEmpty(newPassword)) throw new HttpException(400, "Bad request");
        const salt: string = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(newPassword, salt);
        const updateUserById: IUser = await this.users
            .findByIdAndUpdate(
                foundUser._id,
                { password: hashedPassword },
                { new: true }
            )
            .select("-__v");
		this.sendPasswordNotice(foundUser)
        return updateUserById;
    }

    private async sendPasswordNotice(user: IUser): Promise<void> {
        if (isEmpty(user)) throw new HttpException(400, "Bad Request");
        const foundUser: IUser = await this.users.findById(user._id);
        if (!isEmpty(foundUser)) {
            const transport = nodemailer.createTransport({
                service: "gmail",
                auth: {
                    user: CONFIG.SUPPORT.SUPPORT_EMAIL_HOST_USER,
                    pass: CONFIG.SUPPORT.SUPPORT_EMAIL_HOST_PASSWORD,
                },
            });

            transport.verify((err: Error, success: boolean) => {
                if (!success) {
                    logger.error(err);
                    throw new HttpException(500, "Server error");
                }
            });

            this.ReadHTMLFile(
                __dirname + "/../../public/password_notice.html",
                function (err: Error, html: string) {
                    if (err) throw new HttpException(500, "Server error");
                    var template = Handlebars.compile(html);
                    var replacements = {
                        username: foundUser.name,
                    };
                    var htmlToSend = template(replacements);
                    transport.sendMail({
                        subject: "Cambio de contraseña",
                        to: user.email,
                        from: CONFIG.SUPPORT.SUPPORT_EMAIL.toString(),
                        html: htmlToSend,
                    });
                }
            );
        }
    }

    private ReadHTMLFile(path: string, callback: any) {
        fs.readFile(path, { encoding: "utf-8" }, function (err, html) {
            if (err) {
                callback(err);
                throw err;
            } else {
                callback(null, html);
            }
        });
    }
}

export default UserService;

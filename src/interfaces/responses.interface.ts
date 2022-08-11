import { IUser } from "@interfaces/users.interface";

export interface ResetPasswordResponse {
    cookie: string;
	token: string;
	expiresIn: number | string;
	user: IUser;
}
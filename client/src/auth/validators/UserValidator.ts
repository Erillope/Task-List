
export class UserValidator {

    private static readonly USERNAME_PATTERN = /^[a-zA-Z0-9._-]{3,}$/
    private static readonly GMAIL_PATTERN = /^[a-zA-Z0-9._%+-]+@gmail\.com$/
    private static readonly PHONE_PATTERN = /^\+?[1-9]\d{1,14}$/
    private static readonly PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/ 

    static validateUserName(userName: string): boolean {
        return UserValidator.USERNAME_PATTERN.test(userName)
    }

    static validateAccount(account: string): boolean {
        return UserValidator.GMAIL_PATTERN.test(account) || UserValidator.PHONE_PATTERN.test(account)
    }

    static validatePassword(password: string): boolean {
        return UserValidator.PASSWORD_PATTERN.test(password)
    }

}
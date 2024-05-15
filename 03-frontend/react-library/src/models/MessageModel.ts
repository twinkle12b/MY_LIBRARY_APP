class MessageModel {
    id?: number;
    userEmail?: string;
    question: string;
    adminEmail?: string;
    title: string;
    response?: string;
    closed?: string;


    constructor(title: string, question: string,) {
        this.title = title;
        this.question = question;

    }
}

export default MessageModel;
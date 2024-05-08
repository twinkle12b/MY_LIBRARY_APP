class ReviewModel {

    id: number;
    userEmail: string;
    date: string;
    book_id: number;
    rating: number;
    review_description?: string;


    constructor(id: number, userEmail: string, date: string, book_id: number,
        rating: number,
        review_description: string) {
        this.id = id;
        this.userEmail = userEmail;
        this.date = date;
        this.book_id = book_id;
        this.rating = rating;
        this.review_description = review_description;

    }

}

export default ReviewModel;
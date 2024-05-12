class ReviewModel {

    id: number;
    userEmail: string;
    date: string;
    book_id: number;
    rating: number;
    reviewDescription?: string;


    constructor(id: number, userEmail: string, date: string, book_id: number,
        rating: number,
        reviewDescription: string) {
        this.id = id;
        this.userEmail = userEmail;
        this.date = date;
        this.book_id = book_id;
        this.rating = rating;
        this.reviewDescription = reviewDescription;

    }

}

export default ReviewModel;
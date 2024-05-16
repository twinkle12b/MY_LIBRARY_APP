import { useEffect, useState } from "react";
import ReviewModel from "../../../models/ReviewModel";
import { SpinnerLoading } from "../../utils/SpinnerLoading";
import { Review } from "../Review";
import { Pagination } from "../../utils/Pagination";

export const ReviewListPage: React.FC<{}> = () => {

    const [reviews, setReviews] = useState<ReviewModel[]>([]);
    const [totalStars, setTotalStars] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    //Pagination
    const [currentPage, setCurrentPage] = useState(1);
    const [reviewsPerPage] = useState(5);
    const [totalAmountOfReviews, setToatlAmountOfReviews] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const bookId = (window.location.pathname).split('/')[2];


    // useEffect to load reviews by bookId
    useEffect(() => {
        const fetchBookReviews = async () => {
            const reviewUrl = `${process.env.REACT_APP_API}/reviews/search/findByBookId?bookId=${bookId}&page=${currentPage - 1}&size=${reviewsPerPage}`;
            const responseData = await fetch(reviewUrl);
            if (!responseData.ok) {
                throw new Error("Something Went Wrong");
            }
            const responseDataJson = await responseData.json();
            const responseReviews = responseDataJson._embedded.reviews;
            const loadedReviews : ReviewModel[] = [];

            //Pagination
            setToatlAmountOfReviews(responseDataJson.page.totalElements);
            setTotalPages(responseDataJson.page.totalPages);

            let weightedStarReviews = 0;

            for(const key in responseReviews) {
                loadedReviews.push({
                    id: responseReviews[key].id,
                    userEmail: responseReviews[key].userEmail,
                    date: responseReviews[key].date,
                    book_id: responseReviews[key].bookId,
                    rating: responseReviews[key].rating,
                    reviewDescription: responseReviews[key].reviewDescription,

            });
            }

            setReviews(loadedReviews);
            setIsLoading(false);

        };

        fetchBookReviews().catch((error: any) => {
            setIsLoading(false);
            setHttpError(error.message);
        })

    }, [currentPage])

    if(isLoading) {
        return(
            <SpinnerLoading/>
        );
    }

    if (httpError) {
        return (
            <div className='container m-5'>
                <p>{httpError}</p>
            </div>
        )
    }



    //1*5 = 5
    const indexOfLastReview = currentPage * reviewsPerPage;
    //5-5= 0
    const indexOfFirstReview = indexOfLastReview - reviewsPerPage;

    let lastItem = indexOfLastReview <= totalAmountOfReviews ? indexOfLastReview : totalAmountOfReviews;

    const paginate = (pageNumber : number) => {
        setCurrentPage(pageNumber)
    };


    return (<div className="container m-5">
        <div>
            <h3> Comments: ({reviews.length}) </h3>
            <p>
                {indexOfFirstReview + 1} to {lastItem} of {totalAmountOfReviews} items:
            </p>
            <div className="row">
                {reviews.map(review => (
                    <Review review={review} key={review.id} />
                ))}
            </div>
        </div>
        {totalPages > 1 && <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate}/>}
    </div>);
}
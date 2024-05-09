import { useOktaAuth } from "@okta/okta-react";
import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import ReviewModel from "../../models/ReviewModel";
import { SpinnerLoading } from "../utils/SpinnerLoading";
import { StarReviews } from "../utils/StarReviews";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import { LatestReviews } from "./LatestReviews";

export const BookCheckoutPage = () => {

    //fetch book 

    const [book, setBook] = useState<BookModel>();
    const [isLoading, setLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    //http://localhost:3000/checkout/2 split this using / and grab id
    const bookId = (window.location.pathname).split('/')[2];


    // Reviews State declarations
    const [review, setReviews] = useState<ReviewModel[]>([]);
    const [totalStars, setTotalStars] = useState(0);
    const [isLoadingReviews, setLoadingReviews] = useState(true);

    //Authentication
    const { authState } = useOktaAuth();

    //Loans count state
    const [currentLoansCount, setCurrentLoanCount] = useState(0);
    const [isLoadingCurrentLoansCount, setLoadingCurrentLoansCount] = useState(true);

    //Is book checkedOut By User
    const [isBookCheckedOutByUser, setIsBookCheckedOutByUser] = useState(false)
    const [isLoadingBookCheckedOutByUser, setLoadingBookCheckedOutByUser] = useState(true);


    //function to checkout a book
    async function checkoutBookByUser() {
        //Auth check not required checkoutBookByUser function called 
        //only when checkout button visible for authenticated users
       // if (authState && authState.isAuthenticated) {
            const url = `http://localhost:8080/api/books/secure/checkout?bookId=${bookId}`;
            const requestOptions = {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                    'Content-Type': 'application/json'
                }
            }
            const responseData = await fetch(url, requestOptions);
            if (!responseData.ok) {
                throw new Error('Something went wrong while checking out book');
            }
            //  const responseJson = await responseData.json();
            setIsBookCheckedOutByUser(true);
        //}

    }

    //useEffect for Is book checkedOut By User

    useEffect(() => {
       if(authState && authState.isAuthenticated) {
        const fetchIsBookCheckedOutByUser = async () => {
            const url = `http://localhost:8080/api/books/secure/isBookCheckedOutByUser?bookId=${bookId}`
            const requestOptions = {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                    'Content-type': 'application/json'

                }
            }

            const responseData = await fetch(url, requestOptions);
            if (!responseData.ok) {
                throw new Error('Something went wrong while get checked Out status');
            }
            const responseJson = await responseData.json();
            setIsBookCheckedOutByUser(responseJson);

        }

        fetchIsBookCheckedOutByUser().catch((error: any) => {
            setLoadingBookCheckedOutByUser(false);
            setHttpError(error.message)
        });
    }
        setLoadingBookCheckedOutByUser(false);


    }, [authState])

    // useEffect for currentLoan count
    useEffect(() => {
        const fetchCurrentLoanCountByUser = async () => {
            if (authState && authState.isAuthenticated) {
                const url = `http://localhost:8080/api/books/secure/currentLoanCount`;
                const requestOptions = {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        'Content-Type': 'application/json'
                    }
                }

                const responseData = await fetch(url, requestOptions);
                if (!responseData.ok) {
                    throw new Error("Sometthing went wrong in fetching current Loan")
                }
                const responseJson = await responseData.json();
                setCurrentLoanCount(responseJson);


            }
            setLoadingCurrentLoansCount(false);

        }

        fetchCurrentLoanCountByUser().catch((error: any) => {
            setLoadingCurrentLoansCount(false);
            setHttpError(error.message);
        });

    }, [authState, isBookCheckedOutByUser]);


    // useEffect to load book after user click view Details by bookId
    useEffect(() => {
        const fetchBook = async () => {
            const baseUrl: string = `http://localhost:8080/api/books/${bookId}`

            const response = await fetch(baseUrl);
            if (!response.ok) {
                throw new Error('Something went wrong');
            }
            const responseJson = await response.json();

            const loadedBook: BookModel = {
                id: responseJson.id,
                title: responseJson.title,
                author: responseJson.author,
                description: responseJson.description,
                copies: responseJson.copies,
                copiesAvailable: responseJson.copiesAvailable,
                category: responseJson.category,
                img: responseJson.img,
            }

            setBook(loadedBook);
            setLoading(false);

        };
        fetchBook().catch(
            (error: any) => {
                setLoading(false);
                setHttpError(error.message);
            }
        )

    }, [isBookCheckedOutByUser])


    // useEffect to load reviews by bookId

    useEffect(() => {
        const fetchBookReviews = async () => {
            const reviewUrl = `http://localhost:8080/api/reviews/search/findBookById?bookId=${bookId}`
            const responseReviews = await fetch(reviewUrl);
            if (!responseReviews.ok) {
                throw new Error("Something Went Wrong");
            }
            const responseJsonReviews = await responseReviews.json();
            const responseData = responseJsonReviews._embedded.reviews;
            const loadedReviews: ReviewModel[] = [];
            let weightedStarReviews = 0;

            for (const key in responseData) {
                loadedReviews.push({
                    id: responseData[key].id,
                    userEmail: responseData[key].userEmail,
                    date: responseData[key].date,
                    book_id: responseData[key].bookId,
                    rating: responseData[key].rating,
                    review_description: responseData[key].reviewDescription
                });
                weightedStarReviews += responseData[key].rating;
            }

            if (loadedReviews) {
                const round = (Math.round((weightedStarReviews / loadedReviews.length) * 2) / 2).toFixed(1);
                setTotalStars(Number(round));
            }
            setReviews(loadedReviews);
            setLoadingReviews(false)

        };
        fetchBookReviews().catch((error: any) => {
            setLoadingReviews(false);
            setHttpError(error.message);
        })

    }, [])



    if (isLoading || isLoadingReviews || isLoadingCurrentLoansCount || isLoadingBookCheckedOutByUser) {
        return (
            <SpinnerLoading />
        )
    }

    if (httpError) {
        return (
            <div className='container m-5'>
                <p>{httpError}</p>
            </div>
        )
    }


    return (
        <div>
            <div className='container d-none d-lg-block'>
                <div className='row mt-5'>
                    <div className='col-sm-2 col-md-2'>
                        {book?.img ?
                            <img src={book?.img} width='226' height='349' alt='Book' />
                            :
                            <img src={require('./../../Images/BooksImages/book-luv2code-1000.png')} width='226'
                                height='349' alt='Book' />
                        }
                    </div>
                    <div className='col-4 col-md-4 container'>
                        <div className='ml-2'>
                            <h2>{book?.title}</h2>
                            <h5 className='text-primary'>{book?.author}</h5>
                            <p className='lead'>{book?.description}</p>
                            <StarReviews rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false}
                        currentLoanCount={currentLoansCount} isAuthenticated={authState?.isAuthenticated}
                        isCheckedOut={isBookCheckedOutByUser} checkoutBookByUser={checkoutBookByUser} />
                </div>
                <hr />
                <LatestReviews reviews={review} bookId={book?.id} mobile={false} />
            </div>
            {/* For Mobile Application */}
            <div className='container d-lg-none mt-5'>
                <div className='d-flex justify-content-center align-items-center'>
                    {book?.img ?
                        <img src={book?.img} width='226' height='349' alt='Book' />
                        :
                        <img src={require('./../../Images/BooksImages/book-luv2code-1000.png')} width='226'
                            height='349' alt='Book' />
                    }
                </div>
                <div className='mt-4'>
                    <div className='ml-2'>
                        <h2>{book?.title}</h2>
                        <h5 className='text-primary'>{book?.author}</h5>
                        <p className='lead'>{book?.description}</p>
                        <StarReviews rating={totalStars} size={32} />
                    </div>
                </div>
                <CheckoutAndReviewBox book={book} mobile={true}
                    currentLoanCount={currentLoansCount} isAuthenticated={authState?.isAuthenticated}
                    isCheckedOut={isBookCheckedOutByUser} checkoutBookByUser={checkoutBookByUser}/>
                <hr />
                <LatestReviews reviews={review} bookId={book?.id} mobile={true} />
            </div>
        </div>
    );
}
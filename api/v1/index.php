<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

//including the required files
require_once '../include/DbOperation.php';
require '../vendor/autoload.php';

$app = new \Slim\App();

$container = $app->getContainer();

$container->get('settings')['displayErrorDetails'] = true;


$app->get('/users', function (Request $request, Response $response) {
    $db = new DbOperation();
    $result = $db->getAllUsers();
    
        $data = [];

        $data['users'] = [];

        while($row = $result->fetch_assoc()){
            $user = [];
            $user['user_id'] = $row['user_id'];
            $user['email'] = $row['email'];
            $user['name'] = $row['name'];
            $user['mobile_no'] = $row['mobile_no'];
            $user['photo_url'] = $row['photo_url'];
            $user['date_of_registration'] = $row['date_of_registration'];
            $user['links'] = [];
            $selfLink = [];
            $selfLink['href'] = $request->getUri() . '/' .$user['user_id'];
            $selfLink['rel'] = 'self';
            array_push($user['links'], $selfLink);
            array_push($data['users'],$user);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }
    return $response;
});

$app->get('/users/{uid}', function (Request $request, Response $response) {
    $db = new DbOperation();
    $uid = $request->getAttribute('uid');
    
    $result = $db->getUserByFirebaseUserId($uid);
    
    if($result){
        if($result->num_rows > 0){
            $data = [];
            $data = [];
            $row = $result->fetch_assoc();
            $user = [];
            $user['user_id'] = $row['user_id'];
            $user['name'] = $row['name'];
            $user['email'] = $row['email'];
            $user['mobile_no'] = $row['mobile_no'];
            $user['photo_url'] = $row['photo_url'];
            $user['date_of_registration'] = $row['date_of_registration'];
            $user['links'] = [];
            $selfLink = [];
            $selfLink['href'] = (string)$request->getUri();
            $selfLink['rel'] = 'self';
            array_push($user['links'], $selfLink);
            array_push($data,$user);
            $response = writeResponse($request, $response, $data, 200);
        }
        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $data, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
});

$app->post('/users', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
    
    $name = getValueOrDefault('name',$parsedBody, "");
    $email = getValueOrDefault('email',$parsedBody, "");
    $mobile_no = getValueOrDefault('mobile_no',$parsedBody, "");
    $photo_url = getValueOrDefault('photo_url',$parsedBody, "");
    $date_of_registration = getValueOrDefault('date_of_registration',$parsedBody,"" );
    $firebase_uid = getValueOrDefault('firebase_uid',$parsedBody, "NULL");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createUser($name, $email, $mobile_no, $photo_url, $date_of_registration, $firebase_uid);

    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $id = $res['id'];
        $data["error"] = false;
        //Adding a success message
        $data["message"] = "You are successfully registered";
        //Displaying data
        $response = writeResponse($request, $response, $data, 201);
        $data['links'] = [];
        $selfLink = [];
        $selfLink['href'] = $request->getUri() . $id;
        $selfLink['rel'] = 'self';
        array_push($data['links'], $selfLink);
    //If the result returned is 1 means failure
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while registereing";
        $response = writeResponse($request, $response, $data, 500);

    //If the result returned is 2 means user already exist
    } else if ($res['error'] == 2) {
        $data["error"] = true;
        $data["message"] = "Sorry, this email already existed";
        $response = writeResponse($request, $response, $data, 403);
    }
    return $response;
});


$app->get('/items', function(Request $request, Response $response){
	$db = new DbOperation();
	$result = $db->getAllItems();
	$data = [];

        while($row = $result->fetch_assoc()){
            $item = [];
            $item['id'] = $row['id'];
            $item['price'] = $row['price'];
            $item['name'] = $row['name'];
            $item['category'] = $row['category'];
            $item['description'] = $row['description'];
            $item['imageUri'] = $row['imageUri'];
            array_push($data,$item);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }
    return $response;
});

$app->get('/sid', function(Request $request, Response $response){
	$db = new DbOperation();
	$result = $db->getAllShops();
	$data = [];
	$data['shops'] = [];

        while($row = $result->fetch_assoc()){
            $item = [];
            $item['sid'] = $row['sid'];
            $item['address'] = $row['address'];
            $item['phone'] = $row['phone'];
            $item['sname'] = $row['sname'];
            $item['scategory'] = $row['scategory'];
            array_push($data['shops'],$item);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }
    return $response;
});

$app->get('/items/{id}', function (Request $request, Response $response) {
    $db = new DbOperation();
    $id = $request->getAttribute('id');
    
        $result = $db->getItemById($id);
    
    if($result){
        if($result->num_rows > 0){
            $row = $result->fetch_assoc();
            $user = [];
            $user['id'] = $row['id'];
            $user['price'] = $row['price'];
            $user['name'] = $row['name'];
            $user['category'] = $row['category'];
            $user['description'] = $row['description'];
            $user['imageUri'] = $row['imageUri'];
            $response = writeResponse($request, $response, $user, 200);
        }
        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $data, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
});



$app->post('/items', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
   
    
    //$id = getValueOrDefault('id',$parsedBody, "");
    $price = getValueOrDefault('price',$parsedBody, "");
    $name = getValueOrDefault('name',$parsedBody, "");
    $category = getValueOrDefault('category',$parsedBody, "");
    $description = getValueOrDefault('description',$parsedBody, "");
    $url = getValueOrDefault('imageUri',$parsedBody, "");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createItem($price, $name, $category, $description, $url);

    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $id = $res['id'];
        $data["error"] = false;
        //Adding a success message
        $data["message"] = "You have successfully inserted an item";
        //Displaying data
        $response = writeResponse($request, $response, $data, 201);
        
    //If the result returned is 1 means failure
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while inserting";
        $response = writeResponse($request, $response, $data, 500);

    //If the result returned is 2 means user already exist
    } else if ($res['error'] == 2) {
        $data["error"] = true;
        $data["message"] = "Sorry, this item already existed";
        $response = writeResponse($request, $response, $data, 403);
    }
    return $response;
});

function writeResponse($request,$response,$data,$status_code){
    $response = $response->withHeader('Content-type', 'application/json');
    $response = $response->withJson($data,$status_code);
    return $response;
}

function getValueOrDefault($key,$array,$default){
    return array_key_exists($key,$array) ? $array[$key] : $default;
}

function authorization($request,$response,$next){
    
}

$app->run();



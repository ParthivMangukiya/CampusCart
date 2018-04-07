<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

//including the required files
require_once '../include/DbOperation.php';
require '../vendor/autoload.php';

$app = new \Slim\App();

$container = $app->getContainer();

$container->get('settings')['displayErrorDetails'] = true;

$app->get('/items', function(Request $request, Response $response){
	$db = new DbOperation();
	$result = $db->getAllItems();
	$data = [];
	$data['items'] = [];

        while($row = $result->fetch_assoc()){
            $item = [];
            $item['id'] = $row['id'];
            $item['price'] = $row['price'];
            $item['name'] = $row['name'];
            $item['category'] = $row['category'];
            $item['description'] = $row['description'];
            array_push($data['items'],$item);
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
            $data = [];
            $data['item'] = [];
            $row = $result->fetch_assoc();
            $user = [];
            $user['id'] = $row['id'];
            $user['price'] = $row['price'];
            $user['name'] = $row['name'];
            $user['category'] = $row['category'];
            $user['description'] = $row['description'];
            array_push($data['item'],$user);
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



$app->post('/items', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
    $parsedBody = $parsedBody["item"];
    
    //$id = getValueOrDefault('id',$parsedBody, "");
    $price = getValueOrDefault('price',$parsedBody, "");
    $name = getValueOrDefault('name',$parsedBody, "");
    $category = getValueOrDefault('category',$parsedBody, "");
    $description = getValueOrDefault('description',$parsedBody, "");
    
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createItem($price, $name, $category, $description);

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



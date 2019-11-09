module Player exposing
  ( Model
  , decoder
  , toNameWithAnchor
  )

import Html exposing (..)
import Html.Attributes exposing (..)
import Json.Decode
import Json.Decode.Pipeline
import Url.Builder



type alias Model =
  { rivalId : Int
  , name : String
  }


decoder : Json.Decode.Decoder Model
decoder =
  Json.Decode.succeed Model
    |> Json.Decode.Pipeline.required "rivalId" Json.Decode.int
    |> Json.Decode.Pipeline.required "name" Json.Decode.string


toNameWithAnchor : Model -> Html msg
toNameWithAnchor player =
  a [ href (Url.Builder.relative [] [ Url.Builder.int "rivalIds" player.rivalId ]) ]
    [ text (player.name ++ " (" ++ (String.fromInt player.rivalId) ++ ")")
    ]

;;; seph-mode.el --- Major mode for the seph language

;; Copyright (C) 2010  Ola Bini

;; Author: Ola Bini <ola.bini@gmail.com>
;; Keywords: 

;; This file is free software; you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation; either version 2, or (at your option)
;; any later version.

;; This file is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with GNU Emacs; see the file COPYING.  If not, write to
;; the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
;; Boston, MA 02110-1301, USA.

;;; Commentary:

;; 

;;; Code:

(defconst seph-version "0"
  "seph mode version number")

(defconst seph-interpreter-executable "seph"
  "seph executable")

(defconst seph-indent-offset 2
  "seph mode indent offset")

(defconst seph-electric-parens-p t
  "Should the seph mode autoindent after parentheses are typed?")

(defconst seph-clever-indent-p t
  "Should the seph mode try to dedent and reindent depending on context?")

(defconst seph-auto-mode-p t
  "Should the seph mode add itself to the auto mode list?")

(defgroup seph-font-lock-faces nil
  "Specific Seph faces for highlighting Seph sources."
  :prefix "seph-font-lock-"
  :group (if (featurep 'xemacs)
             'font-lock-faces
           'font-lock-highlighting-faces))


(defface seph-font-lock-object-mimic-face
  '((((class grayscale)) (:foreground "grey"))
    (((class color)) (:foreground "medium blue"))
    (t (:bold t)))
  "Font Lock Mode face used to highlight mimicking of something."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-object-mimic-face 'seph-font-lock-object-mimic-face)

(defface seph-font-lock-operator-name-face
  '((((type tty) (class color)) (:foreground "LightSteelBlue" :weight light))
    (((class grayscale) (background light)) (:foreground "LightGray" :bold t))
    (((class grayscale) (background dark)) (:foreground "DimGray" :bold t))
    (((class color) (background light)) (:foreground "Orchid"))
    (((class color) (background dark)) (:foreground "CornflowerBlue"))
    (t (:bold t)))
  "Font Lock mode face used to highlight operator names."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-operator-name-face 'seph-font-lock-operator-name-face)

(defface seph-font-lock-operator-symbol-face
  '((((type tty) (class color)) (:foreground "LightSteelBlue" :weight light))
    (((class grayscale) (background light)) (:foreground "LightGray" :bold t))
    (((class grayscale) (background dark)) (:foreground "DimGray" :bold t))
    (((class color) (background light)) (:foreground "Orchid"))
    (((class color) (background dark)) (:foreground "deep sky blue"))
    (t (:bold t)))
  "Font Lock mode face used to highlight operator symbols."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-operator-symbol-face 'seph-font-lock-operator-symbol-face)

(defface seph-font-lock-number-face
  '((((class grayscale) (background light)) (:foreground "DimGray" :italic t))
    (((class grayscale) (background dark)) (:foreground "LightGray" :italic t))
    (((class color) (background light)) (:foreground "RosyBrown"))
    (((class color) (background dark)) (:foreground "LightSalmon"))
    (t (:italic t)))
  "Font Lock mode face used to highlight numbers."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-number-face 'seph-font-lock-number-face)

(defface seph-font-lock-known-kind-face
  '((((type tty) (class color)) (:foreground "magenta"))
    (((class grayscale) (background light))
     (:foreground "LightGray" :bold t :underline t))
    (((class grayscale) (background dark))
     (:foreground "Gray50" :bold t :underline t))
    (((class color) (background light)) (:foreground "CadetBlue"))
    (((class color) (background dark)) (:foreground "Aquamarine"))
    (t (:bold t :underline t)))
  "Font Lock mode face used to highlight known kinds."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-known-kind-face 'seph-font-lock-known-kind-face)

(defface seph-font-lock-api-cell-face
  '((((class grayscale) (background light)) (:foreground "DimGray"))
    (((class grayscale) (background dark)) (:foreground "LightGray"))
    (((class color) (background light)) (:foreground "dark goldenrod"))
    (((class color) (background dark)) (:foreground "light goldenrod")))
  "Font Lock mode face used to highlight API cells."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-api-cell-face 'seph-font-lock-api-cell-face)
(defconst seph-font-lock-special-face font-lock-keyword-face)
(defconst seph-font-lock-kind-face font-lock-type-face)

(defface seph-font-lock-object-assign-face
  '((((type tty) (class color)) (:foreground "blue" :weight light))
    (((class grayscale) (background light)) (:foreground "LightGray" :bold t))
    (((class grayscale) (background dark)) (:foreground "DimGray" :bold t))
    (((class color) (background light)) (:foreground "Orchid"))
    (((class color) (background dark)) (:foreground "LightSteelBlue"))
    (t (:bold t)))
  "Font Lock Mode face used to highlight assignment."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-object-assign-face 'seph-font-lock-object-assign-face)
(defconst seph-font-lock-braces-face font-lock-preprocessor-face)
(defconst seph-font-lock-symbol-face font-lock-reference-face)
(defconst seph-font-lock-keyword-argument-face font-lock-reference-face)

(defface seph-font-lock-regexp-face
  '((((type tty) (class color)) (:foreground "DeepPink" :weight light))
    (((class grayscale) (background light)) (:foreground "LightGray" :bold t))
    (((class grayscale) (background dark)) (:foreground "DimGray" :bold t))
    (((class color) (background light)) (:foreground "Orchid"))
    (((class color) (background dark)) (:foreground "DeepPink"))
    (t (:bold t)))
  "Font Lock mode face used to highlight regexps."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-regexp-face 'seph-font-lock-regexp-face)
(defconst seph-font-lock-string-face 'font-lock-string-face)

(defface seph-font-lock-expression-face
  '((((class grayscale) (background light)) (:foreground "DimGray"))
    (((class grayscale) (background dark)) (:foreground "LightGray"))
    (((class color) (background light)) (:foreground "dark goldenrod"))
    (((class color) (background dark)) (:foreground "light goldenrod")))
  "Font Lock mode face used to highlight expressions inside of texts."
  :group 'seph-font-lock-faces)

(defconst seph-font-lock-expression-face 'seph-font-lock-expression-face)

(defconst seph-prototype-names '(
                                 ;; "Base"
                                 ;; "Call"
                                 ;; "Condition"
                                 ;; "DateTime"
                                 ;; "DefaultBehavior"
                                 ;; "DefaultMacro"
                                 ;; "DefaultMethod"
                                 ;; "DefaultSyntax"
                                 ;; "Dict"
                                 ;; "FileSystem"
                                 ;; "Ground"
                                 ;; "SephGround"
                                 ;; "JavaGround"
                                 ;; "Handler"
                                 ;; "IO"
                                 ;; "JavaMethod"
                                 ;; "LexicalBlock"
                                 ;; "LexicalMacro"
                                 ;; "List"
                                 ;; "Message"
                                 ;; "Method"
                                 ;; "Mixins"
                                 ;; "Number"
                                 ;; "Number Decimal"
                                 ;; "Number Integer"
                                 ;; "Number Ratio"
                                 ;; "Number Rational"
                                 ;; "Number Real"
                                 ;; "Origin"
                                 ;; "Pair"
                                 ;; "Range"
                                 ;; "Regexp"
                                 ;; "Rescue"
                                 ;; "Restart"
                                 ;; "Runtime"
                                 ;; "Set"
                                 ;; "Symbol"
                                 ;; "System"
                                 ;; "Text"
                                 "Something"
                                 )
  "seph mode prototype names")

(defconst seph-cell-names '(
                            ;; "print"
                            "println"

                            ;; "cell"
                            ;; "cell?"
                            ;; "documentation"

                            "if"
                            ;; "unless"
                            "while"
                            ;; "until"
                            ;; "loop"
                            ;; "for"
                            ;; "for:set"
                            ;; "for:dict"

                            ;; "bind"
                            ;; "rescue"
                            ;; "handle"
                            ;; "restart"

                            ;; "asText"
                            ;; "inspect"
                            ;; "notice"

                            ;; "do"

                            ;; "call"

                            ;; "list"
                            ;; "dict"
                            ;; "set"
                            "set"
                            "list"
                            "dict"
                            "vector"

                            ;; "with"
                            ;; "kind"
                            )
  "seph mode cell names")

(defconst seph-operator-symbols '(
                "..."
				".."
                "=>"
                "=>>"
                                  
				"++"
				"--"

				"**"
				"*"
				"/"
				"%"

				"+"
				"-"

				"<<"
				">>"

				">"
				"<"
				"<="
				">="
				"<=>"

				"==="
				"=="
				"!="

				"=~"
				"!~"

				"&"

				"^"

                "|"

;                "or"
;                "nor"
;                "xor"
;                "and"
;                "nand"
				"&&"
				"?&"

				"||"
				"?|"

				"<-"
				"<->"
				"->"

				"~"
				"$"
                "+>"
                "!>"
                "<>"
                "&>"
                "%>"
                "#>"
                "@>"
                "/>"
                "*>"
                "?>"
                "|>"
                "^>"
                "~>"
                "**>"
                "&&>"
                "||>"
                "$>"
                "->>"
                "+>>"
                "!>>"
                "<>>"
                "&>>"
                "%>>"
                "#>>"
                "@>>"
                "/>>"
                "*>>"
                "?>>"
                "|>>"
                "^>>"
                "~>>"
                "**>>"
                "&&>>"
                "||>>"
                "$>>"

				"="
				"+="
				"-="
				"*="
				"/="
				"%="
				"&="
				"^="
				"|="
				"<<="
				">>="
				)
  "seph mode operator symbols")

(defconst seph-operator-names '(
                                ;; "return"
                                ;; "break"
                                ;; "continue"

                                "with"
                                "self"
                                ;; "use"

                                ;; "fn"
                                ;; "fnx"
                                ;; "method"
                                ;; "macro"
                                ;; "lecro"
                                ;; "lecrox"
                                ;; "syntax"
                                ;; "dmacro"
                                ;; "dlecro"
                                ;; "dlecrox"
                                ;; "dsyntax"

                                "true"
                                "false"
                                "nil"
                                )
  "seph mode operator names")

(defconst seph-special-names '(
                               "#"
                               ;; "``"
                               ;; "`"
                               ;; "''"
                               ;; "'"
                               ;; "."
                               ;; ","
                               ;; "@"
                               ;; "@@"
                               )
  "seph mode special names")

(defconst seph-standout-names '(
                               "it"
                               )
  "seph mode names that should stand out")

(defconst seph-custom-names '(
			    ; your custom identifiers here
			    )
  "seph mode custom names")

(defconst seph-region-comment-prefix ";"
  "seph region comment prefix")

(defvar seph-mode-hook nil
  "seph mode hook")

(defvar seph-keymap 
  (let ((seph-keymap (make-sparse-keymap)))
    (if seph-electric-parens-p
        (progn
          (define-key seph-keymap "\C-c\C-t" 'seph-eval-buffer)
          (define-key seph-keymap "(" 'seph-electric-open-paren)
          (define-key seph-keymap ")" 'seph-electric-close-paren)
          (define-key seph-keymap "[" 'seph-electric-open-s-paren)
          (define-key seph-keymap "]" 'seph-electric-close-s-paren)
          (define-key seph-keymap "{" 'seph-electric-open-c-paren)
          (define-key seph-keymap "}" 'seph-electric-close-c-paren)
          (define-key seph-keymap (kbd "C-/") 'comment-or-uncomment-region)
          ))
    seph-keymap)
  "seph mode keymap")

(defconst seph-font-lock-keywords
  (list
    '("\\([[:alnum:]!?_:-]+\\)[[:space:]]*=[^=][[:space:]]*[[:alnum:]_:-]+[[:space:]]+mimic" 1 seph-font-lock-object-mimic-face)
    '("\\([[:alnum:]!?_:-]+\\)[[:space:]]*[+*/-]?=[^=]" 1 seph-font-lock-object-assign-face)
    '("%/.*?/[oxpniums]*" 0 seph-font-lock-regexp-face)
    '("%r\\[.*?\\][oxpniums]*" 0 seph-font-lock-regexp-face)
    '("%\\[" 0 seph-font-lock-string-face t)
    '("\\([^\\\\]\\|\\\\\\\\\\)\\(#{[^}]*}\\)" 2 seph-font-lock-expression-face t)
    `(,(regexp-opt seph-prototype-names 'words) . seph-font-lock-known-kind-face)
    `(,(regexp-opt seph-standout-names 'words) . font-lock-warning-face)
    '("\\<[A-Z][[:alnum:]!?_:-]*\\>" 0 seph-font-lock-kind-face)
    '("\\<[[:alnum:]!?_:-]*?:\\>" 0 seph-font-lock-keyword-argument-face)
    '("\\<:[[:alnum:]!?_:-]*\\>" 0 seph-font-lock-symbol-face)
    '("\\<:\"[[:alnum:]!?_:-]*\"" 0 seph-font-lock-symbol-face t)
    '("\\<[[:digit:]_\\.eE]+\\>" 0 seph-font-lock-number-face)
    `(,(regexp-opt seph-operator-names 'words) . seph-font-lock-operator-name-face)
    `(,(regexp-opt seph-operator-symbols t) . seph-font-lock-operator-symbol-face)
    `(,(regexp-opt seph-special-names t) . seph-font-lock-special-face)
    `(,(regexp-opt seph-cell-names 'words) . seph-font-lock-api-cell-face)
    '("[](){}[]" 0 seph-font-lock-braces-face)
   )
  "seph mode font lock keywords")

(defvar seph-syntax-table
  (let ((st (make-syntax-table)))
    (modify-syntax-entry ?\( "()" st)
    (modify-syntax-entry ?\) ")(" st)
    (modify-syntax-entry ?\[ "(]" st)
    (modify-syntax-entry ?\] ")[" st)
    (modify-syntax-entry ?\{ "(}" st)
    (modify-syntax-entry ?\} "){" st)
    (modify-syntax-entry ?\; "<" st)
    (modify-syntax-entry ?? "w" st)
    (modify-syntax-entry ?! "w" st)
    (modify-syntax-entry ?: "w" st)
    (modify-syntax-entry ?, "." st)
    (modify-syntax-entry ?. "." st)
    (modify-syntax-entry ?\' "'" st)
    (modify-syntax-entry ?\` "'" st)
    (modify-syntax-entry ?\" "\"" st)
    (modify-syntax-entry ?\n ">" st)
    st)
  "seph mode syntax table")

(defun seph-eval-buffer () (interactive)
       "Evaluate the buffer with seph."
       (shell-command-on-region (point-min) (point-max) "seph -"))

(defun seph-indent-line ()
  "seph mode indent line"
  (interactive)
  (if (bobp)
      (indent-line-to 0)
      (let (current-depth current-close-flag current-close-open-flag
                          (last-indent 0) last-depth last-close-flag last-close-open-flag first-line)
        (save-excursion
          (let (start-point end-point)
                                        ; get the balance of parentheses on the current line
            (end-of-line)
            (setq end-point (point))
            (beginning-of-line)
            (setq first-line (bobp))
            (setq current-close-flag (looking-at "^[ \\t)]*[])}][ \\t)]*$"))
            (setq current-close-open-flag (looking-at "^\\s-*).*(\\s-*$"))
            (setq start-point (point))
            (setq current-depth (car (parse-partial-sexp start-point end-point)))
                                        ; and the previous non-blank line
            (if (not first-line)
                (progn
                  (while (progn 
                           (forward-line -1)
                           (beginning-of-line)
                           (and (not (bobp))
                                (looking-at "^\\s-*$"))))
                  (setq last-indent (current-indentation))
                  (end-of-line)
                  (setq end-point (point))
                  (beginning-of-line)
                  (setq last-close-flag (looking-at "^[ \\t)]*[])}][ \\t)]*$"))
                  (setq last-close-open-flag (looking-at "^\\s-*).*(\\s-*$"))
                  (setq start-point (point))
                  (setq last-depth (car (parse-partial-sexp start-point end-point))))
                (setq last-depth 0))))

        (let ((depth last-depth))
          (if seph-clever-indent-p
              (setq depth (+ depth
                             (if current-close-flag current-depth 0)
                             (if last-close-flag (- last-depth) 0)
                             (if current-close-open-flag -1 0)
                             (if last-close-open-flag 1 0))))
          (indent-line-to (max 0
                          (+ last-indent
                             (* depth seph-indent-offset))))))))







(defun seph-electric-open-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\()
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil)))

(defun seph-electric-close-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\))
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil))
  (blink-matching-open))

(defun seph-electric-open-c-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\{)
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil)))

(defun seph-electric-close-c-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\})
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil))
  (blink-matching-open))

(defun seph-electric-open-s-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\[)
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil)))

(defun seph-electric-close-s-paren ()
  "seph mode electric close parenthesis"
  (interactive)
  (insert ?\])
  (let ((marker (make-marker)))
    (set-marker marker (point-marker))
    (indent-according-to-mode)
    (goto-char (marker-position marker))
    (set-marker marker nil))
  (blink-matching-open))

(defun seph-comment-region (beg end &optional arg)
  "Comment region for Io."
  (interactive "r\nP")
  (let ((comment-start seph-region-comment-prefix))
    (comment-region beg end arg)))

(defconst seph-font-lock-syntactic-keywords
      '(
        ("#\\(\\[\\)\\([^]\\\\]\\|\\\\\\]\\|\\\\[]\\\\u01234567ntfreb#\"\n]\\)*\\(\\]\\)" (1 "|" t) (3 "|" t))
        ))

(defun seph-mode ()
  "seph mode"
  (interactive)
  (kill-all-local-variables)
  (set-syntax-table seph-syntax-table)
  (use-local-map seph-keymap)

  (make-local-variable 'font-lock-defaults)
  (setq font-lock-defaults '(seph-font-lock-keywords nil nil))

  (make-local-variable 'font-lock-syntax-table)
  (setq font-lock-syntax-table seph-syntax-table)

  (make-local-variable 'font-lock-syntactic-keywords)
  (setq font-lock-syntactic-keywords seph-font-lock-syntactic-keywords)

  (set (make-local-variable 'indent-line-function) 'seph-indent-line)
  (set (make-local-variable 'comment-start) "; ")
  (setq major-mode 'seph-mode)
  (setq mode-name "seph mode")
  (run-hooks 'seph-mode-hook)
  (if seph-auto-mode-p
      (add-to-list 'auto-mode-alist '("\\.sp$" . seph-mode))))

(provide 'seph-mode)
;;; seph-mode.el ends here
